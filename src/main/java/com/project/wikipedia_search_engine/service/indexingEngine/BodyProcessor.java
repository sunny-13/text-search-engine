package com.project.wikipedia_search_engine.service.indexingEngine;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.project.wikipedia_search_engine.util.CommonUtil.isBlankString;
import static com.project.wikipedia_search_engine.util.CommonUtil.toLowerCase;

@Component
public class BodyProcessor {

    private static final Pattern REMOVE_LINKS_PATTERN = Pattern.compile(
            "https?://(?:[a-zA-Z0-9$-_@.&+!*(),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+",
            Pattern.DOTALL
    );
    private static final Pattern REMOVE_TABLES_PATTERN = Pattern.compile("\\{\\|(.*?)\\|\\}", Pattern.DOTALL);
    private static final Pattern REMOVE_CITATIONS_PATTERN = Pattern.compile("\\{\\{v?cite(.*?)\\}\\}", Pattern.DOTALL);
    private static final Pattern REMOVE_HTML_TAGS_PATTERN = Pattern.compile("<(.*?)>", Pattern.DOTALL);

    public String removeUnnecessaryDetails(String bodyText) {
        if(isBlankString(bodyText)) {
            return bodyText;
        }
        bodyText = toLowerCase(bodyText);
        // Remove links
        bodyText = REMOVE_LINKS_PATTERN.matcher(bodyText).replaceAll(" ");
        // Remove tables
        bodyText = REMOVE_TABLES_PATTERN.matcher(bodyText).replaceAll(" ");
        // Remove citations
        bodyText = REMOVE_CITATIONS_PATTERN.matcher(bodyText).replaceAll(" ");
        // Remove HTML tags
        bodyText = REMOVE_HTML_TAGS_PATTERN.matcher(bodyText).replaceAll(" ");
        System.out.println("----");
        System.out.println(bodyText);
        System.out.println("----");
        return bodyText;
    }
}
