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
        String description,
        Long price,
        String curriculum,
        Object regions,
        String timeSlots,
        Long authorUserId,

        // 멘토 정보 (새로 추가)
        String profileImage,
        String sex,
        String mbti,
        String education,
        String major,
        Boolean isCertified,
        String content,
        String appealFileUrl,
        String tag,
        Long mentorId
) {}