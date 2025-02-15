package com.project.wikipedia_search_engine.service.textProcessor.handler.impl;

import com.project.wikipedia_search_engine.service.textProcessor.handler.Lemmatizer;
import com.project.wikipedia_search_engine.service.textProcessor.handler.RegexHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;

@Component
public class ExternalLinksHandler extends Lemmatizer implements RegexHandler {

    @Override
    public String getRegexPattern() {
        return "==External links==\\n[\\s\\S]*?\\n\\n";
    }

    @Override
    public Map<String, Integer> getWordFrequencyMap(String text) {
        if (isBlankString(text)) {
            return new HashMap<>();
        }
        Pattern pattern = Pattern.compile(getRegexPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String matchedString = matcher.group(1);
            stringBuilder.append(matchedString).append(" ");
        }
//        String externalLinksText = stringBuilder.substring(20);
        String externalLinksText = stringBuilder.toString();
        externalLinksText = externalLinksText.replace("|", " ");
        externalLinksText = externalLinksText.replaceAll("[^a-zA-Z ]", " ");
        return getNonStopLemmatizedWordFrequencyMap(externalLinksText);
    }
}
