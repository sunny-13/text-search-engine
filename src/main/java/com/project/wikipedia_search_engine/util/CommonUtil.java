package com.project.wikipedia_search_engine.util;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class CommonUtil {

    public static <T> List<T> nullSafeList(List<T> list) {
        return isNull(list) || list.isEmpty() ? new ArrayList<>() : list;
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
}
