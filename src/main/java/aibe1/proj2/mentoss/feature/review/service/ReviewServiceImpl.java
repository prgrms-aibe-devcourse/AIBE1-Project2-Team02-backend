package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.global.entity.Review;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewMapper reviewMapper;

    @Override
    public void createReview(CreateReviewRequestDto req) {
        Review review = Review.builder()
                .lectureId(req.lectureId())
                .mentorId(req.mentorId())
                .writerId(req.writerId())
                .content(req.content())
                .rating(req.rating())
                .status("AVAILABLE")
                .reportCount(0L)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        reviewMapper.createReview(review);
    }

    @Override
    public void updateReview(Long reviewId, String content, Long rating) {
        reviewMapper.updateReview(reviewId, content, rating);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByLectureId(Long lectureId) {
        return reviewMapper.findByLectureId(lectureId)
                .stream()
                .map(this::reviewToResponseDto)
                .collect(Collectors.toList());
    }

    private ReviewResponseDto reviewToResponseDto(Review r) {
        return new ReviewResponseDto(
                r.getReviewId(),
                r.getLectureId(),
                r.getMentorId(),
                r.getWriterId(),
                r.getContent(),
                r.getRating(),
                r.getCreatedAt()
        );
    }

}
