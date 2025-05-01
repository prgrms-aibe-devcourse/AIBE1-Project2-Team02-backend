package aibe1.proj2.mentoss.feature.lecture.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 시간 정보 요청 DTO - JSON으로 가능한 시간대 저장
 */
@Schema(description = "시간 요청 DTO")
public record TimeSlotRequest(
        @Schema(description = "요일", example = "월")
        String dayOfWeek,

        @Schema(description = "시작 시간", example = "13:00")
        String startTime,

        @Schema(description = "종료 시간", example = "15:00")
        String endTime
) {}

