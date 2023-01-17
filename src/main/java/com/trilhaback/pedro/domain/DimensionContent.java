package com.trilhaback.pedro.domain;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<NodeContent> nodeContentList;

}
