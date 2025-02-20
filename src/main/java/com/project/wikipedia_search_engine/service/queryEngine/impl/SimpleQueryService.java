package com.project.wikipedia_search_engine.service.queryEngine.impl;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import com.project.wikipedia_search_engine.model.RelevancePair;
import com.project.wikipedia_search_engine.model.dto.QueryRequestDTO;
import com.project.wikipedia_search_engine.model.dto.QueryResponseDTO;
import com.project.wikipedia_search_engine.model.enums.RequestField;
import com.project.wikipedia_search_engine.service.queryEngine.QueryService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.PriorityQueue;

import static com.project.wikipedia_search_engine.model.FrequencyModel.getTotalOccurrenceInDoc;
import static com.project.wikipedia_search_engine.util.CommonUtil.isEmptyList;
import static com.project.wikipedia_search_engine.util.CommonUtil.nullSafeList;
import static java.util.Objects.isNull;

@Component
public class SimpleQueryService extends QueryService {

    @Override
    public RequestField getRequestField() {
        return RequestField.SIMPLE;
    }

    @Override
    public QueryResponseDTO getQueryResponse(QueryRequestDTO queryRequestDTO) {
        System.out.println("Here for simple search");
        List<FrequencyModel> frequencyModelList = searchWord(queryRequestDTO.getWord());
        PriorityQueue<RelevancePair> wordRelevanceToDocIDPQ = calculateWordRelevance(frequencyModelList);
        int queryResponseSize = Math.min(queryRequestDTO.getCount(), wordRelevanceToDocIDPQ.size());
        List<String> docTitleList = getDocTitleList(queryResponseSize, wordRelevanceToDocIDPQ);
        return QueryResponseDTO.builder()
                .docTitleList(docTitleList)
                .warning(handleWarningForRequest(queryRequestDTO.getCount(), queryResponseSize))
                .build();
    }

    private PriorityQueue<RelevancePair> calculateWordRelevance(List<FrequencyModel> frequencyModelList) {
        if(isEmptyList(frequencyModelList)) {
            return new PriorityQueue<>();
        }
        BigDecimal inverseDocumentValue = getInverseDocumentValue(frequencyModelList);
        PriorityQueue<RelevancePair> wordRelevanceToDocIDPQ = new PriorityQueue<>(RelevancePair.getComparator());
        for (FrequencyModel frequencyModel : nullSafeList(frequencyModelList)) {
            if(isNull(frequencyModel) || isNull(frequencyModel.getDocId())) continue;
            Integer docId = frequencyModel.getDocId();
            Integer totalOccurrencesInDoc = getTotalOccurrenceInDoc(frequencyModel);
            BigDecimal relevanceInDoc = inverseDocumentValue.multiply(BigDecimal.valueOf(totalOccurrencesInDoc));
            relevanceInDoc = relevanceInDoc.setScale(5, RoundingMode.DOWN);
            wordRelevanceToDocIDPQ.add(new RelevancePair(docId, relevanceInDoc));
        }
        return wordRelevanceToDocIDPQ;
    }
}
