package aibe1.proj2.mentoss.feature.application.model.dto;

public record LectureApplicantDto(
        Long applicationId,
        String nickname,
        String lectureTitle,
        String createdAt,
        String profileImage
) {
}
