package aibe1.proj2.mentoss.feature.application.model.dto;

import java.util.List;

public record LectureApplyRequestDto(
        Long lectureId,
        List<TimeSlot> requestedTimeSlots,
        String message
) {
}
