package com.trilhaback.pedro.service.mapper.dimension;

import com.trilhaback.pedro.domain.dimension.Dimension;
import com.trilhaback.pedro.service.dimension.dto.form.DimensionForm;
import com.trilhaback.pedro.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DimensionFormMapper implements Mapper<DimensionForm, Dimension> {
    @Override
    public Dimension map(DimensionForm dimensionForm) {
        return Dimension.builder()
                .id(dimensionForm.getId())
                .name(dimensionForm.getName())
                .dataType(dimensionForm.getDataType())
                .sonId(dimensionForm.getSonId())
                .build();
    }
}


