package com.project.wikipedia_search_engine.service;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static com.project.wikipedia_search_engine.util.CommonUtil.*;
import static java.util.Objects.isNull;

@Service
public class IndexWriterService {

    private static final String INDEX_FILES_PATH_PREFIX = "src/main/resources/intermediateIndex";
    private static final String FINAL_INDEX_FILE_PATH = "src/main/resources/finalIndex.txt";

    /* Return the file name (path) that is created */
    public String createIntermediateIndexFile(Map<String, List<FrequencyModel>> wordToFrequencyMapList, int intermediateIndexFilesCount) {
        System.out.println("createIntermediateIndexFile");
        String filePath = INDEX_FILES_PATH_PREFIX + intermediateIndexFilesCount + ".txt";
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
        return filePath;
    }

    /* Merges all the index files together and then creates one final Index file for faster retrieval
    * Uses K-Sort merge */
    public void createFinalIndexFile(List<String> intermediateIndexFilePathList) {
        var minHeap = new PriorityQueue<FileEntry>(Comparator.comparing(FileEntry::getWord));
        List<BufferedReader> readers = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FINAL_INDEX_FILE_PATH))) {
            for (String intermediateIndexFilePath : nullSafeList(intermediateIndexFilePathList)) {
                BufferedReader reader = new BufferedReader(new FileReader(intermediateIndexFilePath));
                readers.add(reader);
                String line = reader.readLine();
                if (isNotBlankString(line)) {
                    minHeap.offer(new FileEntry(line, reader));
                }
            }

            String currentWord = null;
            StringBuilder mergedLine = new StringBuilder();
            while (!minHeap.isEmpty()) {
                FileEntry smallest = minHeap.poll();
                String word = smallest.word;
                String data = smallest.fullLine.substring(word.length() + 1); /*Extract FrequencyModel part */

                if (isNull(currentWord)) {
                    /* First word being processed */
                    currentWord = word;
                    mergedLine = new StringBuilder(data);
                } else if (!currentWord.equals(word)) {
                    /* Write the previous merged entry to file */
                    writer.write(currentWord + " " + mergedLine.toString().trim());
                    writer.newLine();

                    /* Start merging for the new word */
                    currentWord = word;
                    mergedLine = new StringBuilder(data);
                } else {
                    /* Same word found in another file, concatenate its frequencyModel data */
                    mergedLine.append(" ").append(data);
                }

                String nextLine = smallest.reader.readLine();
                if (isNotBlankString(nextLine)) {
                    minHeap.offer(new FileEntry(nextLine, smallest.reader));
                }
            }

            /* Write last merged entry */
            if (isNotBlankString(currentWord)) {
                writer.write(currentWord + " " + mergedLine.toString().trim());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            /* Close all readers */
            for (BufferedReader reader : readers) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }
    }

    @Data
    public static class FileEntry {
        private String word;
        private String fullLine;
        private BufferedReader reader;

        public FileEntry(String fullLine, BufferedReader reader) {
            this.fullLine = fullLine;
            this.reader = reader;
            this.word = fullLine.split(" ")[0]; // Extract word (first part of line)
        }
    }
}
