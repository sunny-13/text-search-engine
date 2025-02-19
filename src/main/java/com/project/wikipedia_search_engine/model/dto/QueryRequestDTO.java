package com.project.wikipedia_search_engine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequestDTO {

    private String word;
    @Builder.Default
    private int count = 10;
    private String field;
}
