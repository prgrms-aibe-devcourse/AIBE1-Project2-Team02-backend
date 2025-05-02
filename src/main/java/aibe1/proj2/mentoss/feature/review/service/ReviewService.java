package aibe1.proj2.mentoss.feature.review.service;

import aibe1.proj2.mentoss.feature.review.model.entity.Review;

public interface ReviewService{

    void updateReview(Long reviewId, String content, Long rating);

    void createReview(Review review);

    void deleteReview(Long reviewId);

}
