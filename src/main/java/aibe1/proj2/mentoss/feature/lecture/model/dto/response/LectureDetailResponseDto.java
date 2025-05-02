package aibe1.proj2.mentoss.feature.lecture.model.dto.response;

import java.util.List;

/**
 * 강의 상세 정보 응답 DTO (클라이언트에게 반환)
 */
public record LectureDetailResponseDto(
        Long lectureId,
        String lectureTitle,
        String description,
        Long price,
        List<String> regions,
        List<TimeSlotResponse> timeSlots
) {}