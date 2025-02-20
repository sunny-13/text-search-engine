package com.project.wikipedia_search_engine.service.queryEngine;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import com.project.wikipedia_search_engine.model.RelevancePair;
import com.project.wikipedia_search_engine.model.bean.GlobalDocBean;
import com.project.wikipedia_search_engine.model.dto.QueryRequestDTO;
import com.project.wikipedia_search_engine.model.dto.QueryResponseDTO;
import com.project.wikipedia_search_engine.model.enums.RequestField;
import com.project.wikipedia_search_engine.service.common.DocIdTitleManagerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.*;

import static com.project.wikipedia_search_engine.model.FrequencyModel.parseFrequencyModels;
import static com.project.wikipedia_search_engine.util.CommonUtil.isNotBlankString;
import static com.project.wikipedia_search_engine.util.CommonUtil.toLowerCase;
import static com.project.wikipedia_search_engine.util.Constants.*;
import static java.util.Objects.isNull;

public abstract class QueryService {

    @Autowired
    private DocIdTitleManagerService docIdTitleManagerService;
    @Autowired
    private GlobalDocBean globalDocBean;

    public abstract RequestField getRequestField();

    public abstract QueryResponseDTO getQueryResponse(QueryRequestDTO queryRequestDTO);

    public List<FrequencyModel> searchWord(String searchWord) {
        List<FrequencyModel> frequencyModelList = new ArrayList<>();
        try {
            long offset = findOffsetBinarySearch(toLowerCase(searchWord));
            if (offset == -1) {
                System.out.println("searchWord -> Word not found");
                return Collections.emptyList();
            }
            System.out.println("offset");
            System.out.println(offset);
            /*  Read the specific line from final_index.txt using RandomAccessFile */
            try (RandomAccessFile indexReader = new RandomAccessFile(FINAL_INDEX_FILE_PATH, "r")) {
                indexReader.seek(offset);  /* Jump to the exact byte offset */
                String line = indexReader.readLine(); /* Read the full line */
                System.out.println(line);
                if (line == null || !line.startsWith(toLowerCase(searchWord))) {
                    return Collections.emptyList();
                }

                /* Extract and parse the FrequencyModelList from the line */
                frequencyModelList = parseFrequencyModels(line.substring(searchWord.length() + 1));
            }
        } catch (IOException ex) {
            System.out.println("Exception in fetching offset for word : " + ex.getMessage());
        }
        return frequencyModelList;
    }

    public long findOffsetBinarySearch(String searchWord) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(INDEX_OFFSET_FILE_PATH, "r")) {
            long left = 0;
            long right = raf.length();

            while (left <= right) {
                long mid = (left + right) / 2;
                raf.seek(mid);

                /* Escape partial reading if necessary */
                if (mid != 0) raf.readLine();
                String line = raf.readLine(); /* Read the current line */

                /* This means we have reached the end of the file */
                if (isNull(line)) {
                    right = mid - 1;
                    continue;
                }

                String[] parts = line.split(" ");
                if (parts.length < 2) continue;

                String word = parts[0];
                long offset = Long.parseLong(parts[1]);

                int cmp = word.compareTo(searchWord);
                if (cmp == 0) {
                    return offset;
                } else if (cmp < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1; /* Word not found */
    }

    public List<String> getDocTitleList(int queryResponseSize, PriorityQueue<RelevancePair> wordRelevanceToDocIDPQ) {
        List<String> docTitleList = new ArrayList<>();
        for (int i = 0; i < queryResponseSize; i++) {
            RelevancePair docRelevencePair = wordRelevanceToDocIDPQ.poll();
            if(isNull(docRelevencePair)) continue;
            Integer docId = docRelevencePair.getKey();
            String docTitle = EMPTY_STRING;
            try {
                docTitle = docIdTitleManagerService.getTitleForDocId(String.valueOf(docId));
            } catch (Exception ex) {
                System.out.println("Exception in fetching title for docId : "+ docId);
            }
            if (isNotBlankString(docTitle)) {
                docTitleList.add(docTitle);
            }
        }
        return docTitleList;
    }

    public BigDecimal getInverseDocumentValue(List<FrequencyModel> frequencyModelList) {
        Integer numberOfDocumentContainingWord = frequencyModelList.size();
        return BigDecimal.valueOf(Math.log((double)globalDocBean.getTotalDocCount() / (double)numberOfDocumentContainingWord));
    }

    public static String handleWarningForRequest(int count, int queryResponseSize) {
        if(count > queryResponseSize) {
            return "Can't find " + count + " number of relevant documents";
        }
        return EMPTY_STRING;
    }
}
