package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.AverageRatingResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.CreateReviewRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.global.entity.Review;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.exception.InappropriateContentException;
import aibe1.proj2.mentoss.global.exception.review.InvalidRatingException;
import aibe1.proj2.mentoss.global.exception.ResourceAccessDeniedException;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.review.NotAttendedLectureException;
import aibe1.proj2.mentoss.global.exception.review.NotOwnerException;
import aibe1.proj2.mentoss.global.moderation.model.ModerationResult;
import aibe1.proj2.mentoss.global.moderation.service.ContentModerationService;
import aibe1.proj2.mentoss.global.util.XssSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewMapper reviewMapper;
    private final ContentModerationService contentModerationService;

    @Override
    public void createReview(CreateReviewRequestDto req, Long currentUserId) {
        if (!reviewMapper.existsUser(currentUserId)) {
            throw new ResourceNotFoundException("User", currentUserId);
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
        if (!reviewMapper.hasAttendedLecture(req.lectureId(), currentUserId)) {
            throw new NotAttendedLectureException();
        }

        // XSS 방지를 위한 입력값 정화
        String sanitizedContent = XssSanitizer.sanitize(req.content());

        // AI 유해 콘텐츠 필터링 - 리뷰 내용 검사
        // 부적절한 콘텐츠(욕설, 성적 표현, 혐오 발언, 폭력적 내용, 보안 민감 정보 등)가 포함되어 있는지 확인합니다.
        ModerationResult contentResult = contentModerationService.moderateContent(sanitizedContent);
        if (contentResult.isBlocked()) {
            // 부적절한 콘텐츠가 감지되면 해당 이유와 함께 예외를 발생시킵니다.
            throw new InappropriateContentException(contentResult.getReason());
        }

        Review review = Review.builder()
                .lectureId(req.lectureId())
                .mentorId(req.mentorId())
                .writerId(currentUserId)
                .content(sanitizedContent)
                .rating(req.rating())
                .status("AVAILABLE")
                .reportCount(0L)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        reviewMapper.createReview(review);
    }

    @Override
    public void updateReview(Long reviewId, String content, Long rating, Long currentUserId) {
        if (!reviewMapper.existsReview(reviewId)) {
            throw new ResourceNotFoundException("Review", reviewId);
        }
        if (!reviewMapper.isReviewAccessible(reviewId)) {
            throw new ResourceAccessDeniedException("Review", reviewId);
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new InvalidRatingException("후기 등록 시 별점은 1에서 5 사이의 정수여야 합니다.");
        }
        Long writerId = reviewMapper.findWriterIdByReviewId(reviewId);
        if (!writerId.equals(currentUserId)) {
            throw new NotOwnerException();
        }

        // XSS 방지를 위한 입력값 정화
        String sanitizedContent = XssSanitizer.sanitize(content);

        // AI 유해 콘텐츠 필터링 - 수정된 내용 검사
        ModerationResult moderationResult = contentModerationService.moderateContent(sanitizedContent);
        if (moderationResult.isBlocked()) {
            throw new InappropriateContentException(moderationResult.getReason());
        }

        reviewMapper.updateReview(reviewId, sanitizedContent, rating);

    }

    @Override
    public void deleteReview(Long reviewId, Long currentUserId) {
        if (!reviewMapper.existsReview(reviewId)) {
            throw new ResourceNotFoundException("Review", reviewId);
        }
        if (!reviewMapper.isReviewAccessible(reviewId)) {
            throw new ResourceAccessDeniedException("Review", reviewId);
        }
        Long writerId = reviewMapper.findWriterIdByReviewId(reviewId);
        if (!writerId.equals(currentUserId)) {
            throw new NotOwnerException();
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
        return reviewMapper.findByLectureId(lectureId);
    }

    @Override
    public AverageRatingResponseDto getAverageRatingByLectureId(Long lectureId) {
        if (!reviewMapper.existsLecture(lectureId)) {
            throw new ResourceNotFoundException("Lecture", lectureId);
        }
        if (!reviewMapper.isLectureAccessible(lectureId)) {
            throw new ResourceAccessDeniedException("Lecture", lectureId);
        }
        Double avg = reviewMapper.findAverageRatingByLectureId(lectureId);
        double value = (avg != null) ? avg : 0.0;
        double result = Math.round(value * 10) / 10.0;
        if (result < 0.0 || result > 5.0) {
            throw new InvalidRatingException("평균 별점은 0~5 사이의 소수여야 합니다.");
        }
        return new AverageRatingResponseDto(result, getCountByLecureId(lectureId));
    }

    @Override
    public AverageRatingResponseDto getAverageRatingByMentorId(Long mentorId) {
        if (!reviewMapper.existsMentor(mentorId)) {
            throw new ResourceNotFoundException("Mentor", mentorId);
        }
        Double avg = reviewMapper.findAverageRatingByMentorId(mentorId);
        double value = (avg != null) ? avg : 0.0;
        double result = Math.round(value * 10) / 10.0;
        if (result < 0.0 || result > 5.0) {
            throw new InvalidRatingException("평균 별점은 0~5 사이의 소수여야 합니다.");
        }
        return new AverageRatingResponseDto(result, getCountByMentorId(mentorId));
    }

    private Long getCountByLecureId(Long lectureId){
        return reviewMapper.countReviewsByLectureId(lectureId);
    }

    private Long getCountByMentorId(Long mentorId){
        return reviewMapper.countReviewsByMentorId(mentorId);
    }
}
