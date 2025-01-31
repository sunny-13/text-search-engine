package com.project.wikipedia_search_engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;


@SpringBootApplication
public class WikipediaSearchEngineApplication {

	private static final Logger log = LoggerFactory.getLogger(WikipediaSearchEngineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WikipediaSearchEngineApplication.class, args);
		try {
			String filePath = "/Users/sunny/Downloads/enwiki-20241020-pages-articles-multistream16.xml-p20460153p20570392";
			InputStream inputStream = new FileInputStream(filePath);
			log.info("file found");
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

			String currentElement = null;
			StringBuilder currentText = new StringBuilder();
			while (reader.hasNext()) {
				int event = reader.next();

				switch (event) {
					case XMLStreamReader.START_ELEMENT:
						// Process START_ELEMENT (opening tags)
						currentElement = reader.getLocalName();
						currentText.setLength(0);  // Reset text buffer for the new element

						// Only process <title> and <text> elements
						if ("title".equals(currentElement)) {
							System.out.println("Start Element: " + currentElement);
						}
						break;

					case XMLStreamReader.CHARACTERS:
						// Process CHARACTERS (text inside elements)
						if (currentElement != null && ("title".equals(currentElement))) {
							currentText.append(reader.getText().trim());
						}
						break;

					case XMLStreamReader.END_ELEMENT:
						// Process END_ELEMENT (closing tags)
						if ("title".equals(reader.getLocalName())) {
							System.out.println("Title: " + currentText.toString());
							System.out.println("End Element: " + reader.getLocalName());
						}
						break;
				}
			}
			reader.close();

		} catch (Exception ex) {
			log.error("Can't find file : {}", ex.getMessage());
		}

	}

}
