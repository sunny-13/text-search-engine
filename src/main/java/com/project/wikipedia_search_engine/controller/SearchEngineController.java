package com.project.wikipedia_search_engine.controller;

import com.project.wikipedia_search_engine.service.XMLParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class SearchEngineController {

    @Autowired
    private XMLParserService xmlParserService;

    @GetMapping("/start-server")
    public void startServerAndIndexing() {
        System.out.println("request-received");
        xmlParserService.parseXMLFile();
        System.out.println("response-sent");
    }
}
