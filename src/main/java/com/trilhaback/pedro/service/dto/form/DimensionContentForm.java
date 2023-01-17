package com.trilhaback.pedro.service.dto.form;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.NodeContent;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DimensionContentForm {
    private String id;
    private String name;
    private NodeContent nodeContent;
}
