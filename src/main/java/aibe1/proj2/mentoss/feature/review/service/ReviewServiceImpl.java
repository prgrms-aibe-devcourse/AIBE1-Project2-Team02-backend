package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.entity.Review;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewMapper reviewMapper;

    public void createReview(Review review) {
        reviewMapper.createReview(review);
    }

    public void updateReview(Long reviewId, String content, Long rating) {
        reviewMapper.updateReview(reviewId, content, rating);
    }

    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }

}
