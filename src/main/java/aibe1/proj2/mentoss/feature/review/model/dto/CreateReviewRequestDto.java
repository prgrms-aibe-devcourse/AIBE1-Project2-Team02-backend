package aibe1.proj2.mentoss.feature.review.model.dto;

public record CreateReviewRequestDto(
        Long lectureId,
        Long mentorId,
        String content,
        Long rating) {
}
