package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.UpdateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.service.ReviewService;
import aibe1.proj2.mentoss.global.dto.ApiResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReview(@RequestBody CreateReviewRequestDto dto) {
        reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(null));
    }

    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getByLecture(
            @PathVariable Long lectureId) {
        List<ReviewResponseDto> reviewList = reviewService.getReviewsByLectureId(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(reviewList));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto req) {
        reviewService.updateReview(reviewId, req.content(), req.rating());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }






}
