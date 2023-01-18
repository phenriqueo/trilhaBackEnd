package com.trilhaback.pedro.service.dimension.dto.form;

import com.trilhaback.pedro.domain.dimension.DimensionContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DimensionContentForm {
    private String id;
    private String name;
    private DimensionContent parentContent;
}
