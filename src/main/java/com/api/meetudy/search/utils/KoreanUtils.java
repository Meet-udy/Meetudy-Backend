package com.api.meetudy.search.utils;

public class KoreanUtils {
    private static final char[] CHO =
            {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
                    'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ',
                    'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public static String getInitialConsonants(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isHangulSyllable(c)) {
                int unicode = c - 0xAC00;
                int choIndex = unicode / (21 * 28);
                result.append(CHO[choIndex]);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static boolean isCompleteHangul(String s) {
        for (char c : s.toCharArray()) {
            if (c < 0xAC00 || c > 0xD7A3) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHangulSyllable(char c) {
        return c >= 0xAC00 && c <= 0xD7A3;
    }
}