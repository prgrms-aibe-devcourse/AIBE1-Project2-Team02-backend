package aibe1.proj2.mentoss.global.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

/**
 * XSS 공격을 방지하기 위한 문자열 정화 유틸리티 클래스
 * Tiptap 에디터에서 생성된 HTML을 보존하면서도,
 * <script> 등 위험 요소는 제거합니다.
 */
public class XssSanitizer {

    /**
     * 입력된 문자열에서 XSS 공격 가능성이 있는 HTML 태그와 스크립트를 제거합니다.
     * Tiptap에서 사용하는 태그(mark, span, img 등) 및 스타일 속성(background-color 등)은 허용합니다.
     *
     * @param input 정화할 문자열
     * @return 정화된 문자열 (안전한 HTML)
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        // Tiptap 호환 Safelist 정의
        Safelist safelist = Safelist.relaxed()
                .addTags("span", "img", "mark", "h1", "h2", "h3", "h4")
                .addAttributes("span", "style")
                .addAttributes("mark", "style", "data-color") // mark 태그 스타일 및 data-color 허용
                .addAttributes("p", "style")
                .addAttributes("img", "src", "alt", "title", "width", "height")
                .addProtocols("img", "src", "data", "https");

        // Cleaner 사용
        Document dirty = Jsoup.parseBodyFragment(input, "");
        Cleaner cleaner = new Cleaner(safelist);
        Document clean = cleaner.clean(dirty);

        return clean.body().html();
    }
}