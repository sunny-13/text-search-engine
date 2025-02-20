package com.project.wikipedia_search_engine.service.indexingEngine;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import com.project.wikipedia_search_engine.service.indexingEngine.handler.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;
import static java.util.Objects.nonNull;

@Service
public class TextProcessorService {

    @Autowired
    private BodyProcessor bodyProcessor;
    @Autowired
    private CategoryHandler categoryHandler;
    @Autowired
    private ExternalLinksHandler externalLinksHandler;
    @Autowired
    private ReferencesHandler referencesHandler;
    @Autowired
    private BodyHandler bodyHandler;
    @Autowired
    private TitleHandler titleHandler;

    public void processPage(Map<String, List<FrequencyModel>> wordToFrequencyMapList, String docId, String title, String bodyText) {
        if(isBlankString(title) && isBlankString(bodyText)) {
            return;
        }
        System.out.println(title);
        System.out.println(bodyText);
        Map<String, Integer> titleWordsFreqeuncyMap = titleHandler.getWordFrequencyMap(title);
        System.out.println("TitleHandler:");
        System.out.println(titleWordsFreqeuncyMap);

        bodyText = bodyProcessor.removeUnnecessaryDetails(bodyText);
        Map<String, Integer> categoryWordsFreqeuncyMap = categoryHandler.getWordFrequencyMap(bodyText);
        System.out.println("CategoryHandler:");
        System.out.println(categoryWordsFreqeuncyMap);
        Map<String, Integer> externalLinksWordsFreqeuncyMap = externalLinksHandler.getWordFrequencyMap(bodyText);
        System.out.println("ExternalLinksHandler:");
        System.out.println(externalLinksWordsFreqeuncyMap);
        Map<String, Integer> referencesWordsFreqeuncyMap = referencesHandler.getWordFrequencyMap(bodyText);
        System.out.println("ReferencesHandler:");
        System.out.println(referencesWordsFreqeuncyMap);
        Map<String, Integer> bodyWordsFreqeuncyMap = bodyHandler.getWordFrequencyMap(bodyText);
        System.out.println("BodyHandler:");
        System.out.println(bodyWordsFreqeuncyMap);

        Set<String> wordsSet = Stream.of(titleWordsFreqeuncyMap, categoryWordsFreqeuncyMap,
                        externalLinksWordsFreqeuncyMap, referencesWordsFreqeuncyMap, bodyWordsFreqeuncyMap)
                .flatMap(m -> m.keySet().stream())
                        .collect(Collectors.toSet());
        for (String word : wordsSet) {
            Integer occurrenceInTitle = titleWordsFreqeuncyMap.getOrDefault(word, 0);
            Integer occurrenceInCategory = categoryWordsFreqeuncyMap.getOrDefault(word, 0);
            Integer occurrenceInExternalLinks = externalLinksWordsFreqeuncyMap.getOrDefault(word, 0);
            Integer occurrenceInReferences = referencesWordsFreqeuncyMap.getOrDefault(word, 0);
            Integer occurrenceInBody = bodyWordsFreqeuncyMap.getOrDefault(word, 0);
            FrequencyModel wordFrequencyModel = FrequencyModel.builder()
                    .docId(Integer.parseInt(docId)).titleFrequency(occurrenceInTitle).categoryFrequency(occurrenceInCategory)
                    .externalLinksFrequency(occurrenceInExternalLinks).referencesFrequency(occurrenceInReferences)
                    .bodyFrequency(occurrenceInBody).build();
            List<FrequencyModel> wordExistingFrequencyModelList = nonNull(wordToFrequencyMapList.get(word))
                    ? wordToFrequencyMapList.get(word) : new ArrayList<>();
            wordExistingFrequencyModelList.add(wordFrequencyModel);
            wordToFrequencyMapList.put(word, wordExistingFrequencyModelList);
        }
    }
}
