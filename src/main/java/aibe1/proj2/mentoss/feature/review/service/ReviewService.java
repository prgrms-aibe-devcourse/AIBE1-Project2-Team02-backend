package aibe1.proj2.mentoss.feature.review.service;

import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService{

    void updateReview(Long reviewId, String content, Long rating);

    void createReview(CreateReviewRequestDto req);

    void deleteReview(Long reviewId);

    List<ReviewResponseDto> getReviewsByLectureId(Long lectureId);
}
