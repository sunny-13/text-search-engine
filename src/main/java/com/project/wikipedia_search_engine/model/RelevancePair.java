package com.project.wikipedia_search_engine.model;

import com.project.wikipedia_search_engine.service.SearchEngineService;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

@Data
public class RelevancePair {

    private final Integer key;
    private final BigDecimal value;

    public RelevancePair(Integer key, BigDecimal value) {
        this.key = key;
        this.value = value;
    }

    public static Comparator<RelevancePair> getComparator() {
        return (p1, p2) -> p2.getValue().compareTo(p1.getValue());
    }
}
