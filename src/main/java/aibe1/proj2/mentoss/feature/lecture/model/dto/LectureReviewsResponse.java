package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.util.List;

public record LectureReviewsResponse(
        List<LectureReviewResponse> reviews,
        Double averageRating,
        Long reviewCount
) {
}
