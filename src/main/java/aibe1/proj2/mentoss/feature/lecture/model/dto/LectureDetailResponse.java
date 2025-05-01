package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.util.List;

/**
 * 강의 상세 정보 응답 DTO
 */
public record LectureDetailResponse(
        Long lectureId,
        String lectureTitle,
        String description,
        Long price,
        List<String> regions,
        List<TimeSlotResponse> timeSlots
) {}
