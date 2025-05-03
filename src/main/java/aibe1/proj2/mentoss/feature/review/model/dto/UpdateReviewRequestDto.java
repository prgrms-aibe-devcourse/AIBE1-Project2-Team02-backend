package aibe1.proj2.mentoss.feature.review.model.dto;

public record UpdateReviewRequestDto(
        Long reviewId,
        String content,
        Long rating
) {
}
