package aibe1.proj2.mentoss.feature.report.model.dto.response;

import java.time.LocalDateTime;

public record ReviewDataResponseDto(
        Long reviewId,
        Long lectureId,
        Long mentorId,
        Long writerId,
        String content,
        Long rating,
        String status,
        Long reportCount,
        boolean isDeleted,
        LocalDateTime createdAt
) {
}
