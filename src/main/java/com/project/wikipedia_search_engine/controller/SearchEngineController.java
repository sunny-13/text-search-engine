package com.project.wikipedia_search_engine.controller;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import com.project.wikipedia_search_engine.service.QueryService;
import com.project.wikipedia_search_engine.service.XMLParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("")
public class SearchEngineController {

    @Autowired
    private XMLParserService xmlParserService;
    @Autowired
    private QueryService queryService;

    @GetMapping("/start-server")
    public void startServerAndIndexing() {
        System.out.println("request-received");
        xmlParserService.parseXMLFile();
        System.out.println("response-sent");
    }

    @GetMapping("/search-word")
    public List<FrequencyModel> searchForWord(@RequestParam String word) throws IOException {
        System.out.println("request-received");
        return queryService.searchWord(word);
    }
}
