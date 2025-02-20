package com.project.wikipedia_search_engine.service.indexingEngine.handler;

import java.util.Map;

public interface RegexHandler {

    String getRegexPattern();

    Map<String, Integer> getWordFrequencyMap(String text);
}
