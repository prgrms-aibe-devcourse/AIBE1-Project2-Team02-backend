package aibe1.proj2.mentoss.global.util;

/**
 * XSS 공격을 방지하기 위한 문자열 정화 유틸리티 클래스
 */
public class XssSanitizer {

    /**
     * 입력된 문자열에서 XSS 공격 가능성이 있는 HTML 태그와 스크립트를 제거합니다.
     * 
     * @param input 정화할 문자열
     * @return 정화된 문자열
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        // <img> 태그만 허용하고 나머지 태그 제거
        String sanitized = input.replaceAll("(?i)<(?!img\\b)[^>]*>", ""); // <img>만 통과

        // <script>, javascript: 등 위험한 img src 차단
        sanitized = sanitized.replaceAll("(?i)<img[^>]*src=['\"]?javascript:[^>]*>", "");

        // 특수 문자 이스케이프 처리하되 <img>는 복원
        sanitized = sanitized.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");

        // <img ...> 복원
        sanitized = sanitized.replace("&lt;img", "<img")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#x2F;", "/");


        return sanitized;
    }
}
