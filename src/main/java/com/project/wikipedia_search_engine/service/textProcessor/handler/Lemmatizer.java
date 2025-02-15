package com.project.wikipedia_search_engine.service.textProcessor.handler;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.project.wikipedia_search_engine.util.CommonUtil.*;
import static com.project.wikipedia_search_engine.util.Constants.STOP_WORDS;

@Component
public class Lemmatizer {

    private static final String PROPERTY_KEY = "annotators";
    private static final String PROPERTY_VALUE = "tokenize, ssplit, pos, lemma";

    public Map<String, Integer> getNonStopLemmatizedWordFrequencyMap(String text) {
        if (isBlankString(text)) {
            return new HashMap<>();
        }
        text = trimAndConvertToLowerCase(text);
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_KEY, PROPERTY_VALUE);

        StanfordCoreNLP nlpPipeline = new StanfordCoreNLP(properties);

        CoreDocument coreDocument = new CoreDocument(text);
        nlpPipeline.annotate(coreDocument);
        for (CoreLabel token : nullSafeList(coreDocument.tokens())) {
            String lemma = toLowerCase(token.get(CoreAnnotations.LemmaAnnotation.class));
            if (STOP_WORDS.contains(lemma)) continue;
            wordFrequencyMap.put(lemma, wordFrequencyMap.getOrDefault(lemma, 0) + 1);
        }
        return wordFrequencyMap;
    }

    private String trimAndConvertToLowerCase(String text) {
        text = text.replaceAll("(?<=\\w)\\.(?=\\w)", " ");
        return toLowerCase(text.trim());
    }
}
