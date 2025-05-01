package aibe1.proj2.mentoss.feature.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 시간 정보 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}