package aibe1.proj2.mentoss.feature.account.model.dto;

import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.MentorProfile;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;

import java.util.List;

public record MentorPublicProfileDto(
        Long mentorId,
        String nickname,
        String profileImage,
        String sex,
        Long age,
        String mbti,
        List<RegionDto> regions,
        String education,
        String major,
        String content,
        String tag,
        String appealFileUrl,
        boolean isCertified
) {
    public static MentorPublicProfileDto of(AppUser user, MentorProfile mentorProfile, List<RegionDto> regions) {
        return new MentorPublicProfileDto(
                mentorProfile.getMentorId(),
                user.getNickname(),
                user.getProfileImage(),
                user.getSex(),
                user.getAge(),
                user.getMbti(),
                regions,
                mentorProfile.getEducation(),
                mentorProfile.getMajor(),
                mentorProfile.getContent(),
                mentorProfile.getTag(),
                mentorProfile.getAppealFileUrl(),
                mentorProfile.getIsCertified()
        );
    }
}