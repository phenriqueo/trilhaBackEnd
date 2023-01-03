package com.trilhaback.pedro.service;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.repository.DimensionJDBCRepository;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import com.trilhaback.pedro.service.mapper.DimensionFormMapper;
import com.trilhaback.pedro.service.mapper.DimensionViewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Service
@RequestScope
public class DimensionService {

    DimensionJDBCRepository dimensionJDBCRepository;
    DimensionFormMapper dimensionFormMapper;
    DimensionViewMapper dimensionViewMapper;

    public DimensionService(DimensionJDBCRepository dimesionJDBCRepository,
                            DimensionFormMapper dimensionFormMapper,
                            DimensionViewMapper dimensionViewMapper) {
        this.dimensionJDBCRepository = dimesionJDBCRepository;
        this.dimensionFormMapper = dimensionFormMapper;
        this.dimensionViewMapper = dimensionViewMapper;
    }

    public DimensionView create(DimensionForm dimensionForm) {
        Dimension dimension = dimensionJDBCRepository.insert(dimensionFormMapper.map(dimensionForm));
        return dimensionViewMapper.map(dimension);
    }

    public DimensionView update(DimensionForm dimensionForm) {
        Dimension dimensionCheck = dimensionJDBCRepository.findById(dimensionForm.getId());
        if (dimensionCheck == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dimension " + dimensionForm.getId() + " not found");
        Dimension dimension = dimensionJDBCRepository.update(dimensionFormMapper.map(dimensionForm));
        return dimensionViewMapper.map(dimension);
    }

    public DimensionView findById(Long id) {
        Dimension dimension = dimensionJDBCRepository.findById(id);
        if (dimension == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dimension " + id + " not found");
        return dimensionViewMapper.map(dimension);
    }
}
