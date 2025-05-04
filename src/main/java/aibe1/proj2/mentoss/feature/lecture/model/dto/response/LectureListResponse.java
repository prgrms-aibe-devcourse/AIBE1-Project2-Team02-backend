package aibe1.proj2.mentoss.feature.lecture.model.dto.response;

import java.time.LocalDateTime;

/**
 * 강의 목록 조회용 응답 DTO
 */
public record LectureListResponse(
        Long lectureId,
        String lectureTitle,
        Long price,
        String mentorNickname,
        String profileImage,
        Double averageRating,
        Long reviewCount,
        Boolean isCertified,
        String parentCategory,
        String middleCategory,
        String subcategory,
        LocalDateTime createdAt
) {}
