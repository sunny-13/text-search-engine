package com.project.wikipedia_search_engine.service.common;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;
import static com.project.wikipedia_search_engine.util.Constants.DOC_ID_TITLE_FILE_PATH;
import static com.project.wikipedia_search_engine.util.Constants.EMPTY_STRING;
import static java.util.Objects.isNull;

@Service
public class DocIdTitleManagerService {

    public void createDocIdTitleMapFile(Map<String, String> docIdToTitleMap) {
        System.out.println("createDocIdTitleMapFile");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOC_ID_TITLE_FILE_PATH))) {
            for (Map.Entry<String, String> entry : docIdToTitleMap.entrySet()) {
                String docId = entry.getKey();
                String title = entry.getValue();
                String txtEntry = docId + " " + title;
                writer.write(txtEntry);
                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTitleForDocId(String docId) throws IOException {
        if (isBlankString(docId)) {
            return EMPTY_STRING;
        }
        return findTitleBinarySearch(docId);
    }

    private String findTitleBinarySearch(String docId) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(DOC_ID_TITLE_FILE_PATH, "r")) {
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

                String fetchedDocId = parts[0];
                String fetchedTitle = parts[1];

                int cmp = fetchedDocId.compareTo(docId);
                if (cmp == 0) {
                    return fetchedTitle;
                } else if (cmp < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return EMPTY_STRING; /* Title not found */
    }
}
