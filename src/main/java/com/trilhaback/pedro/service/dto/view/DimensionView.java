package com.trilhaback.pedro.service.dto.view;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.trilhaback.pedro.domain.DataType;
import com.trilhaback.pedro.domain.Dimension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DimensionView {

    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DataType dataType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long sonId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DimensionView> parent;
}
