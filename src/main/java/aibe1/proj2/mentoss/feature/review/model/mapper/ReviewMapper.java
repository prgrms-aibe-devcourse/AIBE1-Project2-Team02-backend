package aibe1.proj2.mentoss.feature.review.model.mapper;

import aibe1.proj2.mentoss.global.entity.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReviewMapper {
    @Select("SELECT * FROM review WHERE id = (#{reviewId})")
    Review findByReviewId(Long reviewId);

    @Select("SELECT * FROM review WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    List<Review> findByLectureId(Long lectureId);

    @Insert("""
    INSERT INTO review (lecture_id, mentor_id, writer_id, content, rating,
                        status, report_count, is_deleted, deleted_at, created_at)
    VALUES (#{lectureId}, #{mentorId}, #{writerId}, #{content}, #{rating}, 
            #{status}, #{reportCount}, #{isDeleted}, #{deletedAt} #{createdAt})
  """)
    void createReview(Review review);

    @Update("""
    UPDATE review
    SET is_deleted = TRUE, deleted_at = CURRENT_TIMESTAMP
    WHERE review_id = #{reviewId}
  """)
    void deleteReview(Long reviewId);


    @Update("""
        UPDATE review
        SET content = #{content}, rating  = #{rating}
        WHERE review_id = #{reviewId}
    """)
    void updateReview(@Param("reviewId") Long reviewId,
                      @Param("content")  String content,
                      @Param("rating")   Long rating);

}
