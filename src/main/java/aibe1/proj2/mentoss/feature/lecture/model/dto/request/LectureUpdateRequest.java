package aibe1.proj2.mentoss.feature.lecture.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 강의 수정 요청 DTO
 */
@Schema(description = "강의 수정 요청 DTO")
public record LectureUpdateRequest(
        @Schema(description = "강의 제목", example = "수정된 강의 제목")
        String lectureTitle,

        @Schema(description = "강의 설명", example = "수정된 강의 설명")
        String description,

        @Schema(description = "카테고리 ID", example = "3")
        Long categoryId,

        @Schema(description = "강의 커리큘럼", example = "수정된 커리큘럼 내용")
        String curriculum,

        @Schema(description = "수강료", example = "35000")
        Long price,

        @Schema(description = "지역 리스트")
        List<LectureRegionRequest> regions,

        @Schema(description = "시간대 리스트")
        List<TimeSlotRequest> timeSlots
) {}
