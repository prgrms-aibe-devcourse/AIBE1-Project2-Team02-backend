package aibe1.proj2.mentoss.feature.review.model.entity;


import java.time.LocalDateTime;

public record Review(
        Long reviewId,
        Long lectureId,
        Long mentorId,
        Long writerId,
        String content,
        Long rating,
        Long status,
        Long reportCount,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt
        ) {

}
