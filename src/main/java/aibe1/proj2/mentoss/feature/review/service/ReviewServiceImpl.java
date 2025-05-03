package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.global.entity.Lecture;
import aibe1.proj2.mentoss.global.entity.Review;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.exception.InvalidRatingException;
import aibe1.proj2.mentoss.global.exception.ResourceAccessDeniedException;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
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
        if (!reviewMapper.existsUser(req.writerId())) {
            throw new ResourceNotFoundException("User", req.writerId());
        }
        if (!reviewMapper.existsLecture(req.lectureId())) {
            throw new ResourceNotFoundException("Lecture", req.lectureId());
        }
        if (!reviewMapper.existsMentor(req.mentorId())) {
            throw new ResourceNotFoundException("Mentor", req.mentorId());
        }
        if (req.rating() == null || req.rating() < 1 || req.rating() > 5) {
            throw new InvalidRatingException("별점은 1에서 5 사이의 정수여야 합니다.");
        }
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
        if (!reviewMapper.existsReview(reviewId)) {
            throw new ResourceNotFoundException("Review", reviewId);
        }
        if (!reviewMapper.isReviewAccessible(reviewId)) {
            throw new ResourceAccessDeniedException("Review", reviewId);
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new InvalidRatingException("별점은 1에서 5 사이의 정수여야 합니다.");
        }
        reviewMapper.updateReview(reviewId, content, rating);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewMapper.existsReview(reviewId)) {
            throw new ResourceNotFoundException("Review", reviewId);
        }
        if (!reviewMapper.isReviewAccessible(reviewId)) {
            throw new ResourceAccessDeniedException("Review", reviewId);
        }
        reviewMapper.deleteReview(reviewId);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByLectureId(Long lectureId) {
        if (!reviewMapper.existsLecture(lectureId)) {
            throw new ResourceNotFoundException("Lecture", lectureId);
        }
        if (!reviewMapper.isLectureAccessible(lectureId)) {
            throw new ResourceAccessDeniedException("Lecture", lectureId);
        }
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
