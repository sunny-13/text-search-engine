package com.project.wikipedia_search_engine.model.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GlobalDocBean { /* This is to track the total number of documents globally in a bean */

    private Integer totalDocCount;

    public GlobalDocBean() {
        this.totalDocCount = 0;
    }
}
