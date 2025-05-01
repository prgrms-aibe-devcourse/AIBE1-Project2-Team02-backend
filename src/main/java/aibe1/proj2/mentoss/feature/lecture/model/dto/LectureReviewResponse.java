package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.time.LocalDateTime;
/**
 * 강의 리뷰 응답 DTO
 */
public record LectureReviewResponse(
        Long reviewId,
        Long lectureId,
        String writerNickname,
        String content,
        Double rating,
        LocalDateTime createdAt
) {}