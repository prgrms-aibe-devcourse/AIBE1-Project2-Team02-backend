package aibe1.proj2.mentoss.feature.report.model.dto.response;

import java.time.LocalDateTime;

public record LectureDataResponseDto(
        Long lectureId,
        Long mentorId,
        String category,
        String curriculum,
        Long price,
        String availableTimeSlots,
        String lectureTitle,
        String description,
        boolean isClosed,
        String status,
        Long reportCount,
        boolean isDeleted,
        LocalDateTime createdAt
) {
}
