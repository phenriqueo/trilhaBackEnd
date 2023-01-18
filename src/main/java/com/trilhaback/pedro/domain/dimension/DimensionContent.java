package com.trilhaback.pedro.domain.dimension;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class DimensionContent {

    private String id;
    private String name;
    private Long dimensionId;
    private DimensionContent parentContent;

}
