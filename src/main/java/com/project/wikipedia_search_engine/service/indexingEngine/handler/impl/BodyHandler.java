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
public class BodyHandler extends Lemmatizer implements RegexHandler {

    private static final Pattern REMOVE_SYMBOLS_PATTERN = Pattern.compile("[^a-zA-Z ]");

    @Override
    public String getRegexPattern() {
        return "== ?[a-z]+ ?==\\n(.*?)\\n";
    }

    @Override
    public Map<String, Integer> getWordFrequencyMap(String text) {
        if (isBlankString(text)) {
            return new HashMap<>();
        }
        Pattern pattern = Pattern.compile("== ?[a-z]+ ?==\\n(.*?)\\n", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String matchedString = matcher.group(1);
//            System.out.println("matchedString");
//            System.out.println(matchedString);
            stringBuilder.append(matchedString).append(" ");
        }
        String bodyText = stringBuilder.toString();
        bodyText = REMOVE_SYMBOLS_PATTERN.matcher(bodyText).replaceAll(" ");
        return getNonStopLemmatizedWordFrequencyMap(bodyText);
    }
}
