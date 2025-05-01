package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.util.List;

/**
 * 강의 리뷰 목록 응답 DTO
 */
public record LectureReviewsResponse(
        List<LectureReviewResponse> reviews,
        Double averageRating,
        Long reviewCount
) {}
