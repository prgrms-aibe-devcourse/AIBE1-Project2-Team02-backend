package aibe1.proj2.mentoss.feature.login.model.dto;

public record UserAccountRequestDTO(
        String nickname,
        String regionCode,
        Long age,
        String sex,
        String mbti
) {
}
