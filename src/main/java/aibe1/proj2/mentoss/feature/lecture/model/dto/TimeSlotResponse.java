package aibe1.proj2.mentoss.feature.lecture.model.dto;

/**
 * 시간 정보 응답 DTO
 */
public record TimeSlotResponse(
        String dayOfWeek,
        String startTime,
        String endTime
) {
}
