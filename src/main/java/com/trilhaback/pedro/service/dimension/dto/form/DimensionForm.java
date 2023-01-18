package com.trilhaback.pedro.service.dimension.dto.form;

import com.trilhaback.pedro.domain.dimension.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DimensionForm {
    private Long id;
    private String name;
    private DataType dataType;
    private Long sonId;
}
