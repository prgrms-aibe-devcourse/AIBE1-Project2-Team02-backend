package aibe1.proj2.mentoss.feature.report.model.dto.response;

import aibe1.proj2.mentoss.feature.lecture.model.dto.response.TimeSlotResponse;
import io.swagger.v3.core.util.Json;

import java.time.LocalDateTime;

public record ApplicationDataResponseDto(
        Long applicationId,
        Long lectureId,
        Long menteeId,
        String requestedTimeSlots,
        String Status,
        boolean is_deleted,
        LocalDateTime createdAt
) {
}
