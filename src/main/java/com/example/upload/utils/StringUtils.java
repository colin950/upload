package com.example.upload.utils;

import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.apache.commons.lang3.StringUtils.length;

public class StringUtils {
    public static boolean isEmpty(@Nullable Object str) { return (str == null || "".equals(str)); }

    public static boolean isNotEmpty(@Nullable Object str) { return !isEmpty(str); }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String camelToSnake(String str) {
        String result = "";

        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);

        for(int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result = result + Character.toLowerCase(ch);
            } else {
                result = result + ch;
            }
        }
        return result;
    }

    public static String convertUTF8(String keyword) throws UnsupportedEncodingException {
        byte[] euckrStringBuffer = keyword.getBytes(Charset.forName("euc-kr"));
        String decodedFromEucKr = new String(euckrStringBuffer, "euc-kr");
        byte[] utf8StringBuffer = decodedFromEucKr.getBytes("utf-8");
        return utf8StringBuffer.toString();
    }
}
