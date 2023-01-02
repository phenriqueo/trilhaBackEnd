package com.trilhaback.pedro.service;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.repository.DimensionJDBCRepository;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import com.trilhaback.pedro.service.mapper.DimensionFormMapper;
import com.trilhaback.pedro.service.mapper.DimensionViewMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

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
        //dimensionJDBCRepository.createDimensionTable(dimension);
    }
}
