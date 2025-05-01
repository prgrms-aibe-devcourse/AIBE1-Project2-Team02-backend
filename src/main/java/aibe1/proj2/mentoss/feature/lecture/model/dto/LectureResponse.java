package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.time.LocalDateTime;

/**
 * 강의 기본 정보 응답 DTO
 */
public record LectureResponse(
        Long lectureId,
        String lectureTitle,
        String mentorNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String parentCategory,
        String middleCategory,
        String subcategory,
        boolean isClosed,
        String status
) {}
