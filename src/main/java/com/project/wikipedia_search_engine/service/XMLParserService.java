package com.project.wikipedia_search_engine.service;

import com.project.wikipedia_search_engine.model.bean.GlobalDocBean;
import com.project.wikipedia_search_engine.model.FrequencyModel;
import com.project.wikipedia_search_engine.service.textProcessor.TextProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class XMLParserService {

    private static final String XML_FILE_PATH = "src/main/resources/regexTesting.xml-p20460153p20570392";
    private static final String PAGE = "page";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final int PAGE_COUNT_PER_WRITE = 1;

    @Autowired
    private TextProcessorService textProcessorService;
    @Autowired
    private IndexWriterService indexWriterService;
    @Autowired
    private DocIdTitleManagerService docIdTitleManagerService;
    @Autowired
    private GlobalDocBean globalDocBean;

    public void parseXMLFile(String xmlFilePath) {
        try {
            XMLStreamReader reader = getXMLStreamReader(xmlFilePath);
            if(isNull(reader)) return;

            /* INITIALIZING VARIABLES */
            List<String> intermediateIndexFilePathList = new ArrayList<>();
            Map<String, String> docIdToTitleMap = new TreeMap<>();
            Integer totalDocCount = 0;
            Map<String, List<FrequencyModel>> wordToFrequencyMapList = new TreeMap<>();
            String currentElement = null;
            StringBuilder currentDocId = new StringBuilder();
            String docId = null;
            boolean idFetched = false; /* There are multiple ids element in the xml pages. We only need to fetch the first root docId*/
            StringBuilder currentTitle = new StringBuilder();
            String title = null;
            StringBuilder currentBodyText = new StringBuilder();
            String bodyText = null;
            int pageCount = 0; /* Refers to the numbers of pages in xml dump <page> .... </page> */
            int intermediateIndexFilesCount = 1;

            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamReader.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        System.out.println("StartElement :");
                        if(PAGE.equals(currentElement)) {
                            currentDocId.setLength(0);
                            currentTitle.setLength(0);
                            currentBodyText.setLength(0);
                            idFetched = false;
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        if(nonNull(currentElement)) {
                            String characters = reader.getText().trim();
                            if (ID.equals(currentElement)) {
                                currentDocId.append(characters);
                            } else if (TITLE.equals(currentElement)) {
                                currentTitle.append(characters);
                            } else if (TEXT.equals(currentElement)) {
                                currentBodyText.append(characters);
                            }
                        }
                        break;

                    case XMLStreamReader.END_ELEMENT:
                        currentElement = reader.getLocalName();
                        if (nonNull(currentElement)) {
                            if (ID.equals(currentElement) && !idFetched) {
                                docId = currentDocId.toString();
                                idFetched = true;
                            } else if (TITLE.equals(currentElement)) {
                                title = currentTitle.toString();
                            } else if (TEXT.equals(currentElement)) {
                                bodyText = currentBodyText.toString();
                            } else if (PAGE.equals(currentElement)) {
                                docIdToTitleMap.put(docId, title);
                                textProcessorService.processPage(wordToFrequencyMapList, docId, title, bodyText);
                                pageCount++;
                                totalDocCount++;
                            }
                        }
                        break;
                }
                if (pageCount == PAGE_COUNT_PER_WRITE) {
                    String intermediateIndexFilePath = indexWriterService.createIntermediateIndexFile(wordToFrequencyMapList, intermediateIndexFilesCount);
                    intermediateIndexFilesCount++;
                    intermediateIndexFilePathList.add(intermediateIndexFilePath);
                    pageCount = 0;
                    wordToFrequencyMapList.clear();
                    wordToFrequencyMapList = new TreeMap<>();
                }
            }
            indexWriterService.createFinalIndexAndOffsetFile(intermediateIndexFilePathList);
            docIdTitleManagerService.createDocIdTitleMapFile(docIdToTitleMap);
            globalDocBean.setTotalDocCount(totalDocCount);

        } catch (Exception ex) {
            System.out.println("file not found");
            System.out.println(ex);
        }
    }

    private XMLStreamReader getXMLStreamReader(String xmlFilePath) {
        try {
            InputStream inputStream = new FileInputStream(xmlFilePath);
            System.out.println("file found");
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            return xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (Exception ex) {
            System.out.println("file not found");
            System.out.println(ex);
        }
        return null;
    }
}
