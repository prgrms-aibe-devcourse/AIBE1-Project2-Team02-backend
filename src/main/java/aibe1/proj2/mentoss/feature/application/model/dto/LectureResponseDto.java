package aibe1.proj2.mentoss.feature.application.model.dto;

public record LectureResponseDto(
        Long lectureId,
        String lectureTitle,
        Long price,
        double averageRating,
        String preferredRegions,
        String subcategory,
        String nickname,
        String profile_image,
        Boolean isCertified,
        Boolean isClosed
) {
}
