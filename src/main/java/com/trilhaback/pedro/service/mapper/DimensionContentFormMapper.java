package com.trilhaback.pedro.service.mapper;

import com.trilhaback.pedro.domain.DimensionContent;
import com.trilhaback.pedro.domain.NodeContent;
import com.trilhaback.pedro.service.dto.form.DimensionContentForm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DimensionContentFormMapper implements Mapper<DimensionContentForm, DimensionContent>{

    @Override
    public DimensionContent map(DimensionContentForm dimensionContentForm) {
        List<NodeContent> nodeContentList = new ArrayList<>();
        nodeContentList.add(dimensionContentForm.getNodeContent());
        return DimensionContent.builder()
                .id(dimensionContentForm.getId())
                .name(dimensionContentForm.getName())
                .nodeContentList(nodeContentList)
                .build();
    }
}
