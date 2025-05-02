package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.UpdateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.service.ReviewService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @Operation(summary = "후기 작성", description = "강의에 대한 후기를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "후기 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ApiResponseFormat<Void>> createReview(@RequestBody CreateReviewRequestDto dto) {
        reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseFormat.ok(null));
    }

    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<ApiResponseFormat<List<ReviewResponseDto>>> getByLecture(
            @PathVariable Long lectureId) {
        List<ReviewResponseDto> reviewList = reviewService.getReviewsByLectureId(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(reviewList));
    }


    @Operation(summary = "후기 수정", description = "후기를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponseFormat<Void>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto req) {
        reviewService.updateReview(reviewId, req.content(), req.rating());
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "후기 삭제", description = "후기를 Soft Delete 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseFormat<Void>> deleteReview(
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }



}
