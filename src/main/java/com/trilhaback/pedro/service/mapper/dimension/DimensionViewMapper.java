package com.trilhaback.pedro.service.mapper.dimension;

import com.trilhaback.pedro.domain.dimension.Dimension;
import com.trilhaback.pedro.service.dimension.dto.view.DimensionView;
import com.trilhaback.pedro.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DimensionViewMapper implements Mapper<Dimension, DimensionView > {

    @Override
    public DimensionView map(Dimension dimension) {
        return DimensionView.builder()
                .id(dimension.getId())
                .name(dimension.getName())
                .dataType(dimension.getDataType())
                .sonId(dimension.getSonId())
                .parent(dimension.getParent().stream().map(dimension1 -> this.map(dimension1)).collect(Collectors.toList()))
                .build();
    }
}
