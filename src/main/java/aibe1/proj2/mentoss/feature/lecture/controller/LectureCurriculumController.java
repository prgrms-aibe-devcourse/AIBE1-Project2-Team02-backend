package aibe1.proj2.mentoss.feature.lecture.controller;

import aibe1.proj2.mentoss.feature.lecture.model.dto.response.LectureCurriculumResponse;
import aibe1.proj2.mentoss.feature.lecture.service.LectureService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lectures")
@Tag(name = "강의 API", description = "강의 커리큘럼 조회 API")
public class LectureCurriculumController {

    private final LectureService lectureService;


    public LectureCurriculumController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/{lectureId}/curriculum")
    @Operation(summary = "강의 커리큘럼 조회", description = "강의의 커리큘럼을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<LectureCurriculumResponse>> getLectureCurriculum(@PathVariable Long lectureId) {
        LectureCurriculumResponse curriculum = lectureService.getLectureCurriculum(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(curriculum));
    }
}
