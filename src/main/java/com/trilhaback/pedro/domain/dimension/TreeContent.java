package com.trilhaback.pedro.domain.dimension;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeContent {
    List<String> nestedHeaders = new ArrayList<>();
    List<List<String>> lines = new ArrayList<>();
}
