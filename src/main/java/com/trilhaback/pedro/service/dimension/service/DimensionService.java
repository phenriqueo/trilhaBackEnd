package com.trilhaback.pedro.service.dimension.service;

import com.trilhaback.pedro.domain.dimension.Dimension;
import com.trilhaback.pedro.domain.dimension.TreeContent;
import com.trilhaback.pedro.repository.dimension.DimensionDDLRepository;
import com.trilhaback.pedro.repository.dimension.DimensionJDBCRepository;
import com.trilhaback.pedro.service.dimension.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dimension.dto.view.DimensionView;
import com.trilhaback.pedro.service.mapper.dimension.DimensionFormMapper;
import com.trilhaback.pedro.service.mapper.dimension.DimensionViewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class DimensionService {

    DimensionJDBCRepository dimensionJDBCRepository;
    DimensionFormMapper dimensionFormMapper;
    DimensionViewMapper dimensionViewMapper;
    DimensionDDLRepository dimensionDDLRepository;
    private static final String firstQuerySelectComponent = "SELECT ID_{0,number,#}.id {1}, ID_{0,number,#}.name {1}_descr";
    private static final String secondQuerySelectComponent = ", ID_{0,number,#}.id {1}, ID_{0,number,#}.name {1}_descr";
    private static final String queryFromComponent = " FROM DIM_{0,number,#} ID_{0,number,#}";
    private static final String queryJoinComponent = " LEFT JOIN DIM_{0,number,#} ID_{0,number,#} ON ID_{0,number,#}.id = ID_{1,number,#}.id_{0,number,#}";

    public DimensionService(DimensionJDBCRepository dimesionJDBCRepository,
                            DimensionFormMapper dimensionFormMapper,
                            DimensionViewMapper dimensionViewMapper,
                            DimensionDDLRepository dimensionDDLRepository) {
        this.dimensionJDBCRepository = dimesionJDBCRepository;
        this.dimensionFormMapper = dimensionFormMapper;
        this.dimensionViewMapper = dimensionViewMapper;
        this.dimensionDDLRepository = dimensionDDLRepository;
    }


    public DimensionView create(DimensionForm dimensionForm) {
        Dimension dimension = dimensionJDBCRepository.insert(dimensionFormMapper.map(dimensionForm));
        dimensionDDLRepository.createDimensionContentTable(dimension);
        return dimensionViewMapper.map(dimension);
    }

    public DimensionView update(DimensionForm dimensionForm) {
        this.findById(dimensionForm.getId());
        Dimension dimension = dimensionJDBCRepository.update(dimensionFormMapper.map(dimensionForm));
        return dimensionViewMapper.map(dimension);
    }

    public DimensionView findById(Long id) {
        Dimension dimension = dimensionJDBCRepository.findById(id);
        if (dimension == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dimension " + id + " not found");
        return dimensionViewMapper.map(dimension);
    }

    public DimensionView findByName(String name) {
        Dimension dimension = dimensionJDBCRepository.findByName(name);
        if (dimension == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dimension " + name + " not found");
        return dimensionViewMapper.map(dimension);
    }

    public void deleteById(Long id) {
        this.findById(id);
        dimensionJDBCRepository.deleteById(id);
        dimensionDDLRepository.dropDimensionContentTable(id);
    }

    public List<DimensionView> findAll() {
        return dimensionJDBCRepository.findAll().stream()
                .map(dimension -> dimensionViewMapper.map(dimension)).collect(Collectors.toList());
    }

    public void addDimensionSon(DimensionForm dimensionForm) {
        dimensionJDBCRepository.findById(dimensionForm.getId());
        dimensionJDBCRepository.addDimensionSon(dimensionFormMapper.map(dimensionForm));
        //dimensionDDLRepository.insertNullLine(dimensionFormMapper.map(dimensionForm));
        dimensionDDLRepository.alterDimensionContentTableSonId(dimensionJDBCRepository.findById(dimensionForm.getId()));
    }

    public DimensionView findTreeById(Long id) {
        this.findById(id);
        return dimensionViewMapper.map(dimensionJDBCRepository.findTreeById(id));
    }

    public void removeSonId(DimensionForm dimensionForm) {
        this.findById(dimensionForm.getId());
        dimensionJDBCRepository.removeSonId(dimensionForm.getId());
        dimensionDDLRepository.dropParentDimensionColumn(dimensionFormMapper.map(dimensionForm));
    }

    public String getQueryForDimensionContentTable(Long id) {
        Dimension dimension = dimensionJDBCRepository.findTreeById(id);
        return MessageFormat.format(firstQuerySelectComponent, dimension.getId(), dimension.getName().replace(" ", "_"))
                + getSelectQueryComponent(dimension)
                + MessageFormat.format(queryFromComponent, dimension.getId())
                + getJoinQueryComponent(dimension)
                + " ORDER BY 1 ASC";
    }

    public String getSelectQueryComponent(Dimension dimension) {
        String selectArgumentsParams = "";
        for (Dimension dimension1 : dimension.getParent()) {
            selectArgumentsParams = selectArgumentsParams
                    + MessageFormat.format(secondQuerySelectComponent, dimension1.getId(), dimension1.getName().replace(" ", "_"))
                    + getSelectQueryComponent(dimension1);
        }
        return selectArgumentsParams;
    }

    public String getJoinQueryComponent(Dimension dimension) {
        String joinArgumentsParams = "";
        for (Dimension dimension1 : dimension.getParent()) {
            joinArgumentsParams = joinArgumentsParams
                    + MessageFormat.format(queryJoinComponent, dimension1.getId(), dimension.getId())
                    + getJoinQueryComponent(dimension1);
        }
        return joinArgumentsParams;
    }

    public TreeContent getDimensionContentTable(Long id) {
        return dimensionJDBCRepository.getDimensionContentTable(this.getQueryForDimensionContentTable(id));
    }
}


