package com.project.wikipedia_search_engine.service.textProcessor.handler;

import java.util.Map;

public interface RegexHandler {

    String getRegexPattern();

    Map<String, Integer> getWordFrequencyMap(String text);
}
