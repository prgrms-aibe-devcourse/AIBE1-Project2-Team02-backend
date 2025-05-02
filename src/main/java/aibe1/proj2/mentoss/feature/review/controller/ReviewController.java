package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.entity.Review;
import aibe1.proj2.mentoss.feature.review.service.ReviewService;
import aibe1.proj2.mentoss.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReview(@RequestBody Review review) {
        Review toSave = new Review(
                null,
                review.lectureId(), review.mentorId(), review.writerId(),
                review.content(),    review.rating(),    review.status(),
                review.reportCount(),review.isDeleted(), review.deletedAt(),
                LocalDateTime.now()
        );
        reviewService.createReview(toSave);
        return ResponseEntity
                .created(URI.create("/api/review/"))
                .body(ApiResponse.ok(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @PathVariable Long id,
            @RequestBody Review review
    ) {
        reviewService.updateReview(id, review.content(), review.rating());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }




}
