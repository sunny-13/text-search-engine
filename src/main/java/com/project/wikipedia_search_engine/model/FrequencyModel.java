package com.project.wikipedia_search_engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyModel {

    private static final String REGEX_PATTERN = "(\\d+)t(\\d+)c(\\d+)e(\\d+)r(\\d+)b(\\d+)";

    private Integer docId;
    private Integer titleFrequency;
    private Integer categoryFrequency;
    private Integer externalLinksFrequency;
    private Integer referencesFrequency;
    private Integer bodyFrequency;

    @Override
    public String toString() {
        return docId + "t" +  titleFrequency + "c" + categoryFrequency + "e" + externalLinksFrequency +
                "r" + referencesFrequency + "b" + bodyFrequency;
    }

    public static List<FrequencyModel> parseFrequencyModels(String data) {
        if(isBlankString(data)) {
            return new ArrayList<>();
        }
        List<FrequencyModel> frequencyModels = new ArrayList<>();
        String[] tokens = data.split(" ");

        for (String token : tokens) {
            Matcher matcher = Pattern.compile(REGEX_PATTERN).matcher(token);
            if (matcher.matches()) {
                FrequencyModel model = new FrequencyModel();
                model.setDocId(Integer.parseInt(matcher.group(1)));
                model.setTitleFrequency(Integer.parseInt(matcher.group(2)));
                model.setCategoryFrequency(Integer.parseInt(matcher.group(3)));
                model.setExternalLinksFrequency(Integer.parseInt(matcher.group(4)));
                model.setReferencesFrequency(Integer.parseInt(matcher.group(5)));
                model.setBodyFrequency(Integer.parseInt(matcher.group(6)));

                frequencyModels.add(model);
            }
        }
        return frequencyModels;
    }
}
