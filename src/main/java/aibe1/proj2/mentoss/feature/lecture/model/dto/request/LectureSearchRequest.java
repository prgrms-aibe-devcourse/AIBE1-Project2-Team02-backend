package aibe1.proj2.mentoss.feature.lecture.model.dto.request;


import java.util.List;

/**
 * 강의 목록 검색 조건 DTO
 */
public record LectureSearchRequest(
        String keyword,         // 키워드 검색
        List<String> categories,  // 카테고리 검색 - String에서 List<String>으로 변경
        List<String> regions,    // 지역 검색
        Long minPrice,          // 최소 가격
        Long maxPrice,          // 최대 가격
        Double minRating,       // 최소 평점
        Boolean isCertified,    // 인증여부
        Boolean isOpen          // 오픈/마감 여부
) {
    // 기존 생성자와 호환성을 위한 생성자
    public LectureSearchRequest(
            String keyword, String category, String region,
            Long minPrice, Long maxPrice, Double minRating,
            Boolean isCertified, Boolean isOpen) {
        this(keyword,
                category != null ? List.of(category) : null,
                region != null ? List.of(region) : null,
                minPrice, maxPrice, minRating, isCertified, isOpen);
    }

    // 기존 호환성을 위한 메소드
    public String category() {
        return categories != null && !categories.isEmpty() ? categories.get(0) : null;
    }
}
