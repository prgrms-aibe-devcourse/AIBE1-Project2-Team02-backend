package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.UpdateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.service.ReviewService;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
@Tag(name = "후기 API", description = "후기 CRUD API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @Operation(summary = "후기 작성", description = "강의에 대한 후기를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "후기 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": true,
            "message": "요청이 성공적으로 처리되었습니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "400", description = "별점 값 오류(1~5 사이의 정수가 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "별점은 1에서 5 사이의 정수여야 합니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "403", description = "본인이 수강한 강의가 아닌 항목에 후기를 작성하려는 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "본인이 수강한 강의가 아닙니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "403", description = "본인이 작성한 후기가 아닌 항목을 수정/삭제하려는 경우",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "본인이 작성한 항목이 아닙니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "404", description = "외래키 참조 오류(참조할 대상 ID가 없음)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Lecture (id=1234) 가 존재하지 않습니다.",
            "data": null
            }
            """)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseFormat<Void>> createReview(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CreateReviewRequestDto dto) {
        reviewService.createReview(dto, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "강의별 후기 조회", description = "특정 강의에 작성된 모든 후기를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                              "success": true,
                                              "message": "요청이 성공적으로 처리되었습니다.",
                                              "data": [
                                                {
                                                  "reviewId": 1,
                                                  "lectureId": 3,
                                                  "mentorId": 1,
                                                  "writerId": 10,
                                                  "content": "강의가 정말 유익했습니다. 내용이 알차고 이해하기 쉬웠어요.",
                                                  "rating": 5,
                                                  "createdAt": [
                                                    2025,
                                                    4,
                                                    26,
                                                    17,
                                                    38,
                                                    31
                                                  ]
                                                },
                                                {
                                                  "reviewId": 2,
                                                  "lectureId": 3,
                                                  "mentorId": 1,
                                                  "writerId": 11,
                                                  "content": "강사님의 설명이 명확하고 예제가 풍부해서 좋았습니다. 다만 초반부는 조금 어려웠어요.",
                                                  "rating": 4,
                                                  "createdAt": [
                                                    2025,
                                                    4,
                                                    28,
                                                    17,
                                                    38,
                                                    31
                                                  ]
                                                }
                                              ]
                                            }
            """))),
            @ApiResponse(responseCode = "403", description = "강의 접근 불가(해당 강의가 삭제되었거나 정지 상태)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Lecture (id=10)에 접근할 수 없습니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "404", description = "외래키 참조 오류(참조할 대상이 없음)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Lecture (id=1234) 가 존재하지 않습니다.",
            "data": null
            }
            """)))

    })
    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<ApiResponseFormat<List<ReviewResponseDto>>> getByLecture(
            @PathVariable Long lectureId) {
        List<ReviewResponseDto> reviewList = reviewService.getReviewsByLectureId(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(reviewList));
    }


    @Operation(summary = "후기 수정", description = "후기를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                               "success": true,
                                               "message": "요청이 성공적으로 처리되었습니다.",
                                               "data": null
                                             }
            """))),
            @ApiResponse(responseCode = "403", description = "후기 접근 불가(해당 후기가 삭제되었거나 정지 상태)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=10) 항목이 삭제되었거나 정지 상태입니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "404", description = "삭제할 대상이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=1234) 가 존재하지 않습니다.",
            "data": null
            }
            """)))

    })
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponseFormat<Void>> updateReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto req) {
        reviewService.updateReview(reviewId, req.content(), req.rating(), user.getUserId());
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "후기 삭제", description = "후기를 삭제(Soft Delete) 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                               "success": true,
                                               "message": "요청이 성공적으로 처리되었습니다.",
                                               "data": null
                                             }
            """))),
            @ApiResponse(responseCode = "403", description = "후기 접근 불가(해당 후기가 이미 삭제되었거나 정지 상태)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=10)에 접근할 수 없습니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "404", description = "삭제할 대상이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=1234) 가 존재하지 않습니다.",
            "data": null
            }
            """)))

    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseFormat<Void>> deleteReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId, user.getUserId());
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }



}
