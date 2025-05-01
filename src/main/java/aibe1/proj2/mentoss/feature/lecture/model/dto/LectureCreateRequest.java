package aibe1.proj2.mentoss.feature.lecture.model.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 강의 생성 요청 DTO
 */
public record LectureCreateRequest(
        String lectureTitle,
        String description,
        Long categoryId,
        String curriculum,
        Long price,
        List<RegionRequest> regions,
        List<TimeSlotRequest> timeSlots
) {}
