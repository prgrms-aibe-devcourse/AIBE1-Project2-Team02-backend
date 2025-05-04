package aibe1.proj2.mentoss.feature.lecture.model.dto.request;

/**
 * 강의 목록 검색 조건 DTO
 */
public record LectureSearchRequest(
        String keyword,         // 키워드 검색
        String category,        // 카테고리 검색
        String region,          // 지역 검색
        Long minPrice,          // 최소 가격
        Long maxPrice,          // 최대 가격
        Double minRating,       // 최소 평점
        Boolean isCertified,    // 인증여부
        Boolean isOpen          // 오픈/마감 여부
) {}
