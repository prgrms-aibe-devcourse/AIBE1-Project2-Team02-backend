package aibe1.proj2.mentoss.feature.report.model.dto.response;

import java.time.LocalDateTime;

public record UserDataResponseDto(
        Long userId,
        String email,
        String nickname,
        Long age,
        String birthDate,
        String sex,
        String profileImage,
        String mbti,
        String role,
        String status,
        Long reportCount,
        boolean isDeleted,
        LocalDateTime createdAt
) {
}
