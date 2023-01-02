package com.trilhaback.pedro.service.mapper;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import org.springframework.stereotype.Component;

@Component
public class DimensionViewMapper implements Mapper<Dimension, DimensionView >{

    @Override
    public DimensionView map(Dimension dimension) {
        return DimensionView.builder()
                .id(dimension.getId())
                .name(dimension.getName())
                .build();
    }
}
