package com.trilhaback.pedro.service.mapper.dimension;

import com.trilhaback.pedro.domain.dimension.DimensionContent;
import com.trilhaback.pedro.service.dimension.dto.view.DimensionContentView;
import com.trilhaback.pedro.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DimensionContentViewMapper implements Mapper<DimensionContent, DimensionContentView> {

    @Override
    public DimensionContentView map(DimensionContent dimensionContent) {
        return DimensionContentView.builder()
                .id(dimensionContent.getId())
                .name(dimensionContent.getName())
                .build();
    }
}
