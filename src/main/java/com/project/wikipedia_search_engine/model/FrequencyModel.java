package com.project.wikipedia_search_engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyModel {

    private String docId;
    private Integer titleFrequency;
    private Integer categoryFrequency;
    private Integer externalLinksFrequency;
    private Integer referencesFrequency;
    private Integer bodyFrequency;

    @Override
    public String toString() {
        return docId + "t" +  titleFrequency + "c" + categoryFrequency + "d" + externalLinksFrequency +
                "e" + referencesFrequency + "b" + bodyFrequency;
    }
}

//Map<String, FrequencyModel> frequencyMap = new TreeMap<>();
