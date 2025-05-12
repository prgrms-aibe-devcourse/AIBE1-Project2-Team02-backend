package aibe1.proj2.mentoss.feature.application.model.dto;


public record LectureApplicantRawDto(
        Long applicationId,
        String nickname,
        String lectureTitle,
        String createdAt,
        String profileImage,
        String requestedTimeSlots // JSON 문자열
) {
}