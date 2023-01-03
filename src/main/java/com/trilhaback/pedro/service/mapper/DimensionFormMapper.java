package com.trilhaback.pedro.service.mapper;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import org.springframework.stereotype.Component;

@Component
public class DimensionFormMapper implements Mapper<DimensionForm, Dimension>{
    @Override
    public Dimension map(DimensionForm dimensionForm) {
        return Dimension.builder()
                .id(dimensionForm.getId())
                .name(dimensionForm.getName())
                .dataType(dimensionForm.getDataType())
                .build();
    }
}


