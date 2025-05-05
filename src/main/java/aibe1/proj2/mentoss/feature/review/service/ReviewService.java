package aibe1.proj2.mentoss.feature.review.service;

import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService{

    void updateReview(Long reviewId, String content, Long rating, Long currentUserId);

    void createReview(CreateReviewRequestDto req, Long currentUserId);

    void deleteReview(Long reviewId, Long currentUserId);

    List<ReviewResponseDto> getReviewsByLectureId(Long lectureId);
}
