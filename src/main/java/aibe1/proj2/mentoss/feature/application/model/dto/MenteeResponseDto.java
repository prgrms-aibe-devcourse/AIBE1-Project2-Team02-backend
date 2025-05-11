package aibe1.proj2.mentoss.feature.application.model.dto;

import java.time.LocalDateTime;

public record MenteeResponseDto(
        Long matchId,
        Long lectureId,
        Long menteeId,
        String nickname,
        String profileImage,
        String lectureTitle,
        String matchedTimeSlots,
        LocalDateTime joinedAt
) {
}