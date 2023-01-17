package com.trilhaback.pedro.service.mapper;

import com.trilhaback.pedro.domain.DimensionContent;
import com.trilhaback.pedro.service.dto.view.DimensionContentView;
import org.springframework.stereotype.Component;

@Component
public class DimensionContentViewMapper implements Mapper<DimensionContent, DimensionContentView>{

    @Override
    public DimensionContentView map(DimensionContent dimensionContent) {
        return DimensionContentView.builder()
                .id(dimensionContent.getId())
                .name(dimensionContent.getName())
                .build();
    }
}
