package com.trilhaback.pedro.service.mapper.dimension;

import com.trilhaback.pedro.domain.dimension.DimensionContent;
import com.trilhaback.pedro.service.dimension.dto.form.DimensionContentForm;
import com.trilhaback.pedro.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DimensionContentFormMapper implements Mapper<DimensionContentForm, DimensionContent> {

    @Override
    public DimensionContent map(DimensionContentForm dimensionContentForm) {
        return DimensionContent.builder()
                .id(dimensionContentForm.getId())
                .name(dimensionContentForm.getName())
                .parentContent(dimensionContentForm.getParentContent())
                .build();
    }
}
