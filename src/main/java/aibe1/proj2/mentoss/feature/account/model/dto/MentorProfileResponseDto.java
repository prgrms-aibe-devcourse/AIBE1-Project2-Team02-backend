package aibe1.proj2.mentoss.feature.account.model.dto;

import aibe1.proj2.mentoss.global.entity.MentorProfile;

import java.time.LocalDateTime;

public record MentorProfileResponseDto(
        Long mentorId,
        Long userId,
        String education,
        String major,
        Boolean isCertified,
        String content,
        String appealFileUrl,
        String tag,
        LocalDateTime createdAt
) {
    public static MentorProfileResponseDto fromEntity(MentorProfile mentorProfile) {
        return new MentorProfileResponseDto(
                mentorProfile.getMentorId(),
                mentorProfile.getUserId(),
                mentorProfile.getEducation(),
                mentorProfile.getMajor(),
                mentorProfile.getIsCertified(),
                mentorProfile.getContent(),
                mentorProfile.getAppealFileUrl(),
                mentorProfile.getTag(),
                mentorProfile.getCreatedAt()
        );
    }
}
