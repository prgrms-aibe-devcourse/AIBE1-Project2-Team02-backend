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
        Long authorUserId,
        String profileImage,
        Double averageRating,
        Long reviewCount,
        Boolean isCertified,
        String parentCategory,
        String middleCategory,
        String subcategory,
        String regions, // 지역 정보 필드 추가
        LocalDateTime createdAt
) {}
