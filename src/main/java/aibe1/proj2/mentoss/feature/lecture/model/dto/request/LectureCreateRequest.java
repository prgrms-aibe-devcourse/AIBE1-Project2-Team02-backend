package aibe1.proj2.mentoss.feature.lecture.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 강의 생성 요청 DTO
 */
@Schema(description = "강의 생성 요청 DTO")
public record LectureCreateRequest(
        @Schema(description = "강의 제목", example = "test강의")
        String lectureTitle,

        @Schema(description = "강의 설명", example = "test내용")
        String description,

        @Schema(description = "카테고리 ID", example = "2")
        Long categoryId,

        @Schema(description = "강의 커리큘럼", example = "test커리큘럼")
        String curriculum,

        @Schema(description = "수강료", example = "30000")
        Long price,

        @Schema(description = "지역 리스트")
        List<LectureRegionRequest> regions,

        @Schema(description = "시간대 리스트")
        List<TimeSlotRequest> timeSlots
) {}
