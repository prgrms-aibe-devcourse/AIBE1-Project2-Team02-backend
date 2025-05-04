package aibe1.proj2.mentoss.feature.account.model.dto;

import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.Region;

import java.time.LocalDateTime;
import java.util.List;

public record ProfileResponseDto(
        Long userId,
        String email,
        String nickname,
        String birthDate,
        Long age,
        String sex,
        String profileImage,
        List<Region> regionCode,
        String mbti,
        String role,
        LocalDateTime createAt
) {
    public static ProfileResponseDto fromEntity(AppUser appUser, List<Region> regions) {
        return new ProfileResponseDto(
                appUser.getUserId(),
                appUser.getEmail(),
                appUser.getNickname(),
                appUser.getBirthDate(),
                appUser.getAge(),
                appUser.getSex(),
                appUser.getProfileImage(),
                regions,
                appUser.getMbti(),
                appUser.getRole(),
                appUser.getCreatedAt()
        );
    }
}
