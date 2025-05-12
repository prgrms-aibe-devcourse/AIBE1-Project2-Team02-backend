package aibe1.proj2.mentoss.feature.report.model.dto.response;

import java.time.LocalDateTime;

public record MentorDataResponseDto(
        Long mentorId,
        Long userId,
        String education,
        String major,
        boolean isCertified,
        String content,
        String appealFileUrl,
        String tag,
        LocalDateTime createdAt
) {
}
