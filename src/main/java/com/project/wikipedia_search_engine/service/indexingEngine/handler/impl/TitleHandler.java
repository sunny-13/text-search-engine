package com.project.wikipedia_search_engine.service.indexingEngine.handler.impl;

import com.project.wikipedia_search_engine.service.indexingEngine.handler.Lemmatizer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;

@Component
public class TitleHandler extends Lemmatizer {

    public Map<String, Integer> getWordFrequencyMap(String text) {
        if (isBlankString(text)) {
            return new HashMap<>();
        }
        return getNonStopLemmatizedWordFrequencyMap(text);
    }
}
