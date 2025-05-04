package aibe1.proj2.mentoss.feature.application.model.dto;

public record AppliedLectureResponseDto(
        Long applicationId,
        String status,
        String lectureTitle,
        Long price,
        String nickname,
        Boolean isCertified,
        double averageRating,
        String preferredRegions,
        String subcategory,
        String profile_image
) {
}
