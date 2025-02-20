package com.project.wikipedia_search_engine.service;

import com.project.wikipedia_search_engine.model.dto.QueryRequestDTO;
import com.project.wikipedia_search_engine.model.dto.QueryResponseDTO;
import com.project.wikipedia_search_engine.service.queryEngine.QueryServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;
import static java.util.Objects.isNull;

@Service
public class SearchEngineService {

    private static final String NULL_REQUEST_WARNING = "Request Body can't be Null";

    @Autowired
    private XMLParserService xmlParserService;
    @Autowired
    private QueryServiceFactory queryServiceFactory;
    
    public void startServer(String xmlFilePath) {
        xmlParserService.parseXMLFile(xmlFilePath);
    }
    
    public QueryResponseDTO performQuery(QueryRequestDTO queryRequestDTO) {
        if(isNull(queryRequestDTO) || isBlankString(queryRequestDTO.getWord())) {
            return QueryResponseDTO.builder()
                    .warning(NULL_REQUEST_WARNING)
                    .build();
        }
        return queryServiceFactory.getQueryService(queryRequestDTO.getField())
                .getQueryResponse(queryRequestDTO);
    }
}
