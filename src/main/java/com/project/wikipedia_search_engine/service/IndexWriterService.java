package com.project.wikipedia_search_engine.service;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class IndexWriterService {

    private static final String INDEX_FILES_PATH = "src/main/resources/output";

    public void createIntermediateIndexFile(Map<String, List<FrequencyModel>> wordToFrequencyMapList, int intermediateIndexFilesCount) {
        System.out.println("createIntermediateIndexFile");
        String filePath = INDEX_FILES_PATH + intermediateIndexFilesCount + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, List<FrequencyModel>> entry : wordToFrequencyMapList.entrySet()) {
                String word = entry.getKey();
                List<FrequencyModel> models = entry.getValue();
                StringBuilder line = new StringBuilder(word);
                for (FrequencyModel model : models) {
                    line.append(" ").append(model.toString());
                }
                writer.write(line.toString());
                writer.newLine(); // Move to the next line
            }
            System.out.println("Data successfully written to " + filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
