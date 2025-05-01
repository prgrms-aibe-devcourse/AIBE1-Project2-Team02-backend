package aibe1.proj2.mentoss.feature.lecture.model.dto;

/**
 * 시간 정보 요청 DTO - JSON으로 가능한 시간대 저장
 */
public record TimeSlotRequest(
        String dayOfWeek,  // 요일 (MONDAY, TUESDAY, ...)
        String startTime,  // 시작 시간 (HH:MM)
        String endTime     // 종료 시간 (HH:MM)
) {}
