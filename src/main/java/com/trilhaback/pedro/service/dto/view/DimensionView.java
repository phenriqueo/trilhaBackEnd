package com.trilhaback.pedro.service.dto.view;


import com.trilhaback.pedro.domain.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DimensionView {

    private Long id;
    private String name;
    private DataType dataType;

}
