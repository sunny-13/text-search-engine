package com.project.wikipedia_search_engine.service;

import com.project.wikipedia_search_engine.model.FrequencyModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;

import static com.project.wikipedia_search_engine.model.FrequencyModel.parseFrequencyModels;
import static com.project.wikipedia_search_engine.util.Constants.FINAL_INDEX_FILE_PATH;
import static com.project.wikipedia_search_engine.util.Constants.INDEX_OFFSET_FILE_PATH;
import static java.util.Objects.isNull;

@Service
public class QueryService {

    public List<FrequencyModel> searchWord(String searchWord) throws IOException {
        long offset = findOffsetBinarySearch(searchWord);
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
            if (line == null || !line.startsWith(searchWord)) {
                return Collections.emptyList();
            }

            /* Extract and parse the FrequencyModelList from the line */
            return parseFrequencyModels(line.substring(searchWord.length() + 1));
        }
    }

    private long findOffsetBinarySearch(String searchWord) throws IOException {
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

}
