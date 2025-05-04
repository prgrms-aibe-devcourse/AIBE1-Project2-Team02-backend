package aibe1.proj2.mentoss.feature.lecture.controller;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureCreateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureUpdateRequest;
import aibe1.proj2.mentoss.feature.lecture.model.dto.response.*;
import aibe1.proj2.mentoss.feature.lecture.service.LectureService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ApiResponseFormat<Long>> createLecture(
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
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseFormat.ok(lectureId));
    }



    @GetMapping
    @Operation(summary = "강의 목록 조회", description = "강의 목록을 검색 조건에 따라 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<Page<LectureListResponse>>> getLectures(
            @Parameter(description = "검색 키워드 (제목, 내용, 멘토 닉네임)")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "카테고리 (대/중/소분류)")
            @RequestParam(required = false) String category,

            @Parameter(description = "지역 검색")
            @RequestParam(required = false) String region,

            @Parameter(description = "최소 가격")
            @RequestParam(required = false) Long minPrice,

            @Parameter(description = "최대 가격")
            @RequestParam(required = false) Long maxPrice,

            @Parameter(description = "최소 평점")
            @RequestParam(required = false) Double minRating,

            @Parameter(description = "멘토 인증 여부")
            @RequestParam(required = false) Boolean isCertified,

            @Parameter(description = "강의 오픈 여부 (true: 오픈, false: 마감)")
            @RequestParam(required = false) Boolean isOpen,

            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") int size
    ) {
        // 페이지 요청 객체 생성 (스프링 데이터 활용)
        Pageable pageable = PageRequest.of(page, size);

        // 검색 조건 DTO 생성
        LectureSearchRequest searchRequest = new LectureSearchRequest(
                keyword, category, region, minPrice, maxPrice,
                minRating, isCertified, isOpen);

        // 서비스 호출
        Page<LectureListResponse> lectures = lectureService.getLectures(searchRequest, pageable);

        return ResponseEntity.ok(ApiResponseFormat.ok(lectures));
    }

    @GetMapping("/{lectureId}")
    @Operation(summary = "강의 정보 조회", description = "강의의 모든 정보를 조회합니다.")
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
    public ResponseEntity<ApiResponseFormat<Object>> getLecture(@PathVariable Long lectureId) throws JsonProcessingException {
        LectureResponse lecture = lectureService.getLecture(lectureId);

        // 클라이언트에게 반환할 형태로 가공
        Map<String, Object> response = new HashMap<>();
        response.put("lectureId", lecture.lectureId());
        response.put("lectureTitle", lecture.lectureTitle());
        response.put("mentorNickname", lecture.mentorNickname());
        response.put("createdAt", lecture.createdAt());
        response.put("updatedAt", lecture.updatedAt());
        response.put("parentCategory", lecture.parentCategory());
        response.put("middleCategory", lecture.middleCategory());
        response.put("subcategory", lecture.subcategory());
        response.put("isClosed", lecture.isClosed());
        response.put("status", lecture.status());
        response.put("description", lecture.description());
        response.put("price", lecture.price());
        response.put("curriculum", lecture.curriculum());

        // JSON 문자열을 객체로 파싱
        List<String> regionList = null;
        if (lecture.regions() != null && !lecture.regions().isEmpty()) {
            regionList = objectMapper.readValue(lecture.regions(), new TypeReference<List<String>>() {});
        }
        response.put("regions", regionList);

        List<TimeSlotResponse> timeSlotList = null;
        if (lecture.timeSlots() != null && !lecture.timeSlots().isEmpty()) {
            timeSlotList = objectMapper.readValue(lecture.timeSlots(), new TypeReference<List<TimeSlotResponse>>() {});
        }
        response.put("timeSlots", timeSlotList);

        return ResponseEntity.ok(ApiResponseFormat.ok(response));
    }





    @PutMapping("/{lectureId}")
    @Operation(summary = "강의 정보 수정", description = "강의의 정보를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<LectureResponse>> updateLecture(
            @PathVariable Long lectureId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "강의 수정 요청 예시",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LectureUpdateRequest.class),
                            examples = @ExampleObject(
                                    name = "강의 수정 예시",
                                    value = """
{
  "lectureTitle": "수정된 강의 제목",
  "description": "수정된 강의 설명",
  "categoryId": 3,
  "curriculum": "수정된 커리큘럼",
  "price": 35000,
  "regions": [
    { "regionCode": "1111010400" },
    { "regionCode": "1111010500" }
  ],
  "timeSlots": [
    { "dayOfWeek": "화", "startTime": "14:00", "endTime": "16:00" },
    { "dayOfWeek": "목", "startTime": "19:00", "endTime": "21:00" }
  ]
}
"""
                            )
                    )
            )
            @RequestBody LectureUpdateRequest request
    ) throws JsonProcessingException {
        boolean result = lectureService.updateLecture(lectureId, request);

        if (!result) {
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail("강의 수정에 실패했습니다."));
        }

        // 수정된 강의 정보 조회 (업데이트 시간 포함)
        LectureResponse updatedLecture = lectureService.getLecture(lectureId);

        return ResponseEntity.ok(ApiResponseFormat.ok(updatedLecture));
    }



    @DeleteMapping("/{lectureId}")
    @Operation(summary = "강의 삭제", description = "강의를 삭제합니다 (소프트 삭제).")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<Void>> deleteLecture(@PathVariable Long lectureId) {
        boolean result = lectureService.deleteLecture(lectureId);

        if (!result) {
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail("강의 삭제에 실패했습니다."));
        }

        // 삭제 성공 시 204 No Content 반환 (본문 없음)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}





