package com.jd.laf.binding.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 字符串工具
 */
public abstract class Strings {

    /**
     * 按照字符串分割
     *
     * @param value
     * @param delimiter
     * @return
     */
    public static List<String> split(final String value, final String delimiter) {
        List<String> result = new LinkedList<String>();
        if (delimiter == null || delimiter.isEmpty()) {
            return split(value, ',');
        } else if (delimiter.length() == 1) {
            return split(value, delimiter.charAt(0));
        }
        int length = value.length();
        int maxPos = delimiter.length() - 1;
        int start = 0;
        int pos = 0;
        int end = 0;
        for (int i = 0; i < length; i++) {
            if (value.charAt(i) == delimiter.charAt(pos)) {
                if (pos++ == maxPos) {
                    if (end > start) {
                        result.add(value.substring(start, end + 1));
                    }
                    pos = 0;
                    start = i + 1;
                }
            } else {
                end = i;
            }
        }
        if (start < length) {
            result.add(value.substring(start, length));
        }
        return result;
    }

    /**
     * 按照字符分割
     *
     * @param value
     * @param delimiter
     * @return
     */
    public static List<String> split(final String value, final char delimiter) {
        List<String> result = new LinkedList<String>();
        int len = value.length();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (value.charAt(i) == delimiter) {
                if (i > start) {
                    result.add(value.substring(start, i));
                }
                start = i + 1;
            }
        }
        if (start < len) {
            result.add(value.substring(start, len));
        }
        return result;
    }
}
