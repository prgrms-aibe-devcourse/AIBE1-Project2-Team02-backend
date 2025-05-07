package aibe1.proj2.mentoss.feature.lecture.model.dto.request;


import java.util.List;

/**
 * 강의 목록 검색 조건 DTO
 */
public record LectureSearchRequest(
        String keyword,         // 키워드 검색
        String category,        // 카테고리 검색
        List<String> regions,    // 지역 검색 - 배열로 변경
        Long minPrice,          // 최소 가격
        Long maxPrice,          // 최대 가격
        Double minRating,       // 최소 평점
        Boolean isCertified,    // 인증여부
        Boolean isOpen          // 오픈/마감 여부
) {
    // 필터 호환성을 위한 생성자
    public LectureSearchRequest(
            String keyword, String category, String region,
            Long minPrice, Long maxPrice, Double minRating,
            Boolean isCertified, Boolean isOpen) {
        this(keyword, category,
                region != null ? List.of(region) : null,
                minPrice, maxPrice, minRating, isCertified, isOpen);
    }
}
