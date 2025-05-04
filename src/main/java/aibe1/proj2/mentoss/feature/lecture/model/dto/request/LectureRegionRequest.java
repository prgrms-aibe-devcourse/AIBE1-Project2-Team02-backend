package aibe1.proj2.mentoss.feature.lecture.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 지역 정보 요청 DTO
 */
@Schema(description = "지역 요청 DTO")
public record LectureRegionRequest(
        @Schema(description = "법정동 지역코드", example = "1111010400")
        String regionCode
) {}
