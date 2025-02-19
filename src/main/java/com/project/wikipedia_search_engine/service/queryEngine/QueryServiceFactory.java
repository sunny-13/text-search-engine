package com.project.wikipedia_search_engine.service.queryEngine;

import com.project.wikipedia_search_engine.model.enums.RequestField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.wikipedia_search_engine.util.CommonUtil.nullSafeList;

@Component
public class QueryServiceFactory {

    private final Map<String, QueryService> queryServiceMap;

    @Autowired
    public QueryServiceFactory(List<QueryService> queryServiceList) {
        Map<String, QueryService> tempMap = new HashMap<>();
        for (QueryService queryService : nullSafeList(queryServiceList)) {
            tempMap.put(queryService.getRequestField().name(), queryService);
        }
        queryServiceMap = Collections.unmodifiableMap(tempMap);
    }

    public QueryService getQueryService(String requestField) {
        return queryServiceMap.getOrDefault(requestField, getDefaultQueryService());
    }

    private QueryService getDefaultQueryService() {
        return queryServiceMap.get(RequestField.SIMPLE.name());
    }
}
