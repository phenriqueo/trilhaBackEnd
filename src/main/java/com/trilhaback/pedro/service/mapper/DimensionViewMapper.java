package com.trilhaback.pedro.service.mapper;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DimensionViewMapper implements Mapper<Dimension, DimensionView >{

    @Override
    public DimensionView map(Dimension dimension) {
        return DimensionView.builder()
                .id(dimension.getId())
                .name(dimension.getName())
                .dataType(dimension.getDatatype())
                .sonId(dimension.getSonId())
                .parent(dimension.getParent().stream().map(dimension1 -> this.map(dimension1)).collect(Collectors.toList()))
                .build();
    }
}
