package aibe1.proj2.mentoss.feature.application.model.dto;

public record LectureApplyFormRawDto(
        Long lectureId,
        String lectureTitle,
        String availableTimeSlots, // JSON 문자열
        String profileImage,
        String nickname,
        String education,
        String major,
        boolean isCertified,
        double averageRating
) {
}
