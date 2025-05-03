package aibe1.proj2.mentoss.feature.lecture.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record LectureResponse(
        // 기본 정보
        Long lectureId,
        String lectureTitle,
        String mentorNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String parentCategory,
        String middleCategory,
        String subcategory,
        boolean isClosed,
        String status,

        // 상세 정보 (필수 포함)
        String description,
        Long price,
        String curriculum,
        String regions,
        String timeSlots
) {}