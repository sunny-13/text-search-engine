package com.project.wikipedia_search_engine.service.indexingEngine.handler.impl;

import com.project.wikipedia_search_engine.service.indexingEngine.handler.Lemmatizer;
import com.project.wikipedia_search_engine.service.indexingEngine.handler.RegexHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;

@Component
public class ReferencesHandler extends Lemmatizer implements RegexHandler {

    @Override
    public String getRegexPattern() {
        return "== ?references ?==(.*?)\\n\\n";
    }

    @Override
    public Map<String, Integer> getWordFrequencyMap(String text) {
        if (isBlankString(text)) {
            return new HashMap<>();
        }
        Pattern pattern = Pattern.compile(getRegexPattern(), Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String matchedString = matcher.group(1);
            stringBuilder.append(matchedString).append(" ");
        }

        String referencesStr = stringBuilder.toString();
        referencesStr = referencesStr.replaceAll("[^a-zA-Z ]", " ");
        return getNonStopLemmatizedWordFrequencyMap(referencesStr);
    }
}
