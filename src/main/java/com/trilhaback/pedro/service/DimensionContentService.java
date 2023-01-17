package com.trilhaback.pedro.service;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.DimensionContent;
import com.trilhaback.pedro.repository.DimensionContentJDBCRepository;
import com.trilhaback.pedro.repository.DimensionJDBCRepository;
import com.trilhaback.pedro.service.dto.form.DimensionContentForm;
import com.trilhaback.pedro.service.dto.view.DimensionContentView;
import com.trilhaback.pedro.service.mapper.DimensionContentFormMapper;
import com.trilhaback.pedro.service.mapper.DimensionContentViewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class DimensionContentService {

    DimensionContentFormMapper dimensionContentFormMapper;
    DimensionContentViewMapper dimensionContentViewMapper;
    DimensionContentJDBCRepository dimensionContentJDBCRepository;
    DimensionJDBCRepository dimensionJDBCRepository;

    public DimensionContentService(DimensionContentFormMapper dimensionContentFormMapper,
                                   DimensionContentViewMapper dimensionContentViewMapper,
                                   DimensionContentJDBCRepository dimensionContentJDBCRepository,
                                   DimensionJDBCRepository dimensionJDBCRepository) {
        this.dimensionContentFormMapper = dimensionContentFormMapper;
        this.dimensionContentViewMapper = dimensionContentViewMapper;
        this.dimensionContentJDBCRepository = dimensionContentJDBCRepository;
        this.dimensionJDBCRepository = dimensionJDBCRepository;
    }

    public void create(DimensionContentForm dimensionContentForm, Long dimensionId) {
        Dimension dimension = dimensionJDBCRepository.findById(dimensionId);
        dimensionContentJDBCRepository.insert(dimensionContentFormMapper.map(dimensionContentForm), dimension);
    }

    public void update(DimensionContentForm dimensionContentForm, Long dimensionId) {
        Dimension dimension = dimensionJDBCRepository.findById(dimensionId);
        dimensionContentJDBCRepository.update(dimensionContentFormMapper.map(dimensionContentForm), dimension);
    }

    public DimensionContentView findById(Long dimensionId, String dimensionContentId) {
        DimensionContent dimensionContent = dimensionContentJDBCRepository.findById(dimensionId, dimensionContentId);
        if (dimensionContent.getId() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dimension content " + dimensionContentId + " not found");
        return dimensionContentViewMapper.map(dimensionContent);
    }

    public List<DimensionContentView> findAll(Long dimensionId) {
        return dimensionContentJDBCRepository.findAll(dimensionId).stream()
                .map(dimensionContent -> dimensionContentViewMapper.map(dimensionContent)).collect(Collectors.toList());
    }

    public void deleteById(Long dimensionId, String dimensionContentId) {
        this.findById(dimensionId, dimensionContentId);
        dimensionContentJDBCRepository.deleteById(dimensionId, dimensionContentId);
    }

    public void addDimensionRelationship(Long dimensionId, DimensionContentForm dimensionContentForm) {
        dimensionContentJDBCRepository.addDimensionRelationship(dimensionId, dimensionContentFormMapper.map(dimensionContentForm));
    }

}
