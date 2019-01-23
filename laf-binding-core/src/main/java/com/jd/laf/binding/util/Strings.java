package com.jd.laf.binding.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 字符串工具
 */
public abstract class Strings {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

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

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    protected static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /**
     * A table of hex digits
     */
    protected static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * 按照Properties格式存储
     * Converts unicodes to encoded &#92;uxxxx and escapes
     * special characters with a preceding slash
     *
     * @param builder
     * @param value
     * @param escapeSpace
     * @param escapeUnicode
     * @return
     */
    public static StringBuilder store(final StringBuilder builder, final String value,
                                      final boolean escapeSpace, final boolean escapeUnicode) {
        if (value == null) {
            return builder;
        }
        int len = value.length();
        char aChar;
        for (int x = 0; x < len; x++) {
            aChar = value.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    builder.append('\\');
                    builder.append('\\');
                    continue;
                }
                builder.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) {
                        builder.append('\\');
                    }
                    builder.append(' ');
                    break;
                case '\t':
                    builder.append('\\');
                    builder.append('t');
                    break;
                case '\n':
                    builder.append('\\');
                    builder.append('n');
                    break;
                case '\r':
                    builder.append('\\');
                    builder.append('r');
                    break;
                case '\f':
                    builder.append('\\');
                    builder.append('f');
                    break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    builder.append('\\');
                    builder.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                        builder.append('\\');
                        builder.append('u');
                        builder.append(toHex((aChar >> 12) & 0xF));
                        builder.append(toHex((aChar >> 8) & 0xF));
                        builder.append(toHex((aChar >> 4) & 0xF));
                        builder.append(toHex(aChar & 0xF));
                    } else {
                        builder.append(aChar);
                    }
            }
        }
        return builder;
    }

    /**
     * 按照Properties格式存储字符串
     *
     * @param builder
     * @param properties
     * @return
     */
    public static StringBuilder store(final StringBuilder builder, final Properties properties) {
        if (builder != null && properties != null) {
            int count = 0;
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (count++ > 0) {
                    builder.append(LINE_SEPARATOR);
                }
                store(builder, (String) entry.getKey(), true, false).append('=');
                store(builder, (String) entry.getValue(), false, false);
            }
        }
        return builder;
    }

    /**
     * 按照Properties格式输出字符串
     *
     * @param properties
     * @return
     */
    public static String toString(final Properties properties) {
        if (properties == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(8192);
        store(builder, properties);
        return builder.toString();
    }
}
