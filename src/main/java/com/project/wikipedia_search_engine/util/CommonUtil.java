package com.project.wikipedia_search_engine.util;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class CommonUtil {

    private static final String EMPTY_STRING = "";

    public static <T> List<T> nullSafeList(List<T> list) {
        return isNull(list) || list.isEmpty() ? new ArrayList<>() : list;
    }

    public static String nullSafeString(String text) {
        return isNull(text) ? EMPTY_STRING : text;
    }

    public static String toLowerCase(String text) {
        if (isBlankString(text)) {
            return text;
        }
        return text.toLowerCase();
    }

    public static boolean isBlankString(String text) {
        return isNull(text) || text.isBlank();
    }

    public static boolean isNotBlankString(String text) {
        return !isBlankString(text);
    }
}
