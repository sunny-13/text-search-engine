package com.project.wikipedia_search_engine.controller;

import com.project.wikipedia_search_engine.model.dto.QueryRequestDTO;
import com.project.wikipedia_search_engine.model.dto.QueryResponseDTO;
import com.project.wikipedia_search_engine.service.SearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class SearchEngineController {

    @Autowired
    private SearchEngineService searchEngineService;

    @GetMapping("/start-indexing")
    public void startServerAndIndexing(@RequestParam(name = "xmlFilePath") String xmlFilePath) {
        System.out.println("request-received");
        searchEngineService.startServer(xmlFilePath);
        System.out.println("response-sent");
    }

    @GetMapping("/perform-query")
    public QueryResponseDTO performQuery(@RequestBody QueryRequestDTO queryRequestDTO) {
        System.out.println("request-received" + queryRequestDTO);
        return searchEngineService.performQuery(queryRequestDTO);
    }
}
