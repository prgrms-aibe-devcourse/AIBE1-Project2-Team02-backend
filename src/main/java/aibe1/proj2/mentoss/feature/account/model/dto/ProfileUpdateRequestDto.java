package aibe1.proj2.mentoss.feature.account.model.dto;

import java.util.List;

public record ProfileUpdateRequestDto(
        String nickname,
        String birthDate,
        String sex,
        List<String> regionCodes,
        String mbti
) {
}