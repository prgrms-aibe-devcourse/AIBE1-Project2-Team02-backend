package aibe1.proj2.mentoss.feature.lecture.controller;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.feature.lecture.service.LectureService;
import aibe1.proj2.mentoss.global.dto.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@Tag(name = "강의 API", description = "강의 생성 및 조회 관련 API")
public class LectureController {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper;

    public LectureController(LectureService lectureService, ObjectMapper objectMapper) {
        this.lectureService = lectureService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Operation(summary = "강의 생성", description = "새로운 강의를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "강의 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<Long>> createLecture(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "강의 생성 요청 예시",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LectureCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "강의 생성 예시",
                                    value = """
{
  "lectureTitle": "test강의",
  "description": "test내용",
  "categoryId": 2,
  "curriculum": "test커리큘럼",
  "price": 30000,
  "regions": [
    { "regionCode": "1111010400" }
  ],
  "timeSlots": [
    { "dayOfWeek": "월", "startTime": "13:00", "endTime": "15:00" },
    { "dayOfWeek": "월", "startTime": "18:00", "endTime": "20:00" },
    { "dayOfWeek": "화", "startTime": "10:00", "endTime": "12:00" }
  ]
}
"""
                            )
                    )
            )
            @RequestBody LectureCreateRequest request
    ) throws JsonProcessingException {
        Long lectureId = lectureService.createLecture(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(lectureId));
    }

    @GetMapping("/{lectureId}")
    @Operation(summary = "강의 기본 정보 조회", description = "강의의 기본 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<LectureResponse>> getLecture(@PathVariable Long lectureId) {
        LectureResponse lecture = lectureService.getLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(lecture));
    }

    @GetMapping("/{lectureId}/detail")
    @Operation(summary = "강의 상세 정보 조회", description = "강의의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<LectureDetailResponseDto>> getLectureDetail(@PathVariable Long lectureId) throws JsonProcessingException {
        // 서비스에서 상세 정보 조회
        LectureDetailResponse lectureDetail = lectureService.getLectureDetail(lectureId);

        // JSON 문자열을 객체 리스트로 파싱
        List<TimeSlotResponse> timeSlots = objectMapper.readValue(
                lectureDetail.timeSlots(),  // Record 필드 직접 접근
                new TypeReference<List<TimeSlotResponse>>() {}
        );

        // 클라이언트에게 반환할 DTO 생성
        LectureDetailResponseDto responseDto = new LectureDetailResponseDto(
                lectureDetail.lectureId(),  // Record 필드 직접 접근
                lectureDetail.lectureTitle(),
                lectureDetail.description(),
                lectureDetail.price(),
                lectureDetail.regions(),
                timeSlots
        );

        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    @GetMapping("/{lectureId}/curriculum")
    @Operation(summary = "강의 커리큘럼 조회", description = "강의의 커리큘럼을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<LectureCurriculumResponse>> getLectureCurriculum(@PathVariable Long lectureId) {
        LectureCurriculumResponse curriculum = lectureService.getLectureCurriculum(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(curriculum));
    }

    @GetMapping("/{lectureId}/reviews")
    @Operation(summary = "강의 리뷰 조회", description = "강의의 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<LectureReviewsResponse>> getLectureReviews(@PathVariable Long lectureId) {
        LectureReviewsResponse reviews = lectureService.getLectureReviews(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(reviews));
    }
}