package aibe1.proj2.mentoss.feature.review.model.mapper;

import aibe1.proj2.mentoss.global.entity.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReviewMapper {

    @Select("SELECT COUNT(*) FROM review WHERE review_id = #{reviewId}")
    int countReview(Long reviewId);
    default boolean existsReview(Long reviewId) {
        return countReview(reviewId) > 0;
    }

    @Select("SELECT COUNT(*) FROM review WHERE review_id = #{reviewId} AND status = 'AVAILABLE' AND is_deleted = FALSE")
    int countActiveReview(Long reviewId);
    default boolean isReviewAccessible(Long reviewId) {
        return countActiveReview(reviewId) > 0;
    }

    @Select("SELECT COUNT(*) FROM app_user WHERE user_id = #{userId}")
    int countUser(Long userId);
    default boolean existsUser(Long userId) {
        return countUser(userId) > 0;
    }

    @Select("SELECT COUNT(*) FROM app_user WHERE user_id = #{userId} AND status = 'AVAILABLE' AND is_deleted = FALSE")
    int countActiveUser(Long userId);
    default boolean isUserAccessible(Long userId) {
        return countActiveLecture(userId) > 0;
    }

    @Select("SELECT COUNT(*) FROM lecture WHERE lecture_id = #{lectureId}")
    int countLecture(Long lectureId);
    default boolean existsLecture(Long lectureId) {
        return countLecture(lectureId) > 0;
    }

    @Select("SELECT COUNT(*) FROM lecture WHERE lecture_id = #{lectureId} AND status = 'AVAILABLE' AND is_deleted = FALSE")
    int countActiveLecture(Long lectureId);
    default boolean isLectureAccessible(Long lectureId) {
        return countActiveLecture(lectureId) > 0;
    }

    @Select("SELECT COUNT(*) FROM mentor_profile WHERE mentor_id = #{mentorId}")
    int countMentor(Long mentorId);
    default boolean existsMentor(Long mentorId) {
        return countMentor(mentorId) > 0;
    }

    @Select("SELECT user_id FROM mentor_profile WHERE mentor_id=#{mentorId}")
    Long getUserByMentorId(Long mentorId);

    @Select("SELECT * FROM review WHERE lecture_id = #{lectureId} AND is_deleted = FALSE")
    @Results({
            @Result(property = "reviewId",  column = "review_id"),
            @Result(property = "lectureId", column = "lecture_id"),
            @Result(property = "mentorId",  column = "mentor_id"),
            @Result(property = "writerId",  column = "writer_id"),
            @Result(property = "createdAt", column = "created_at")
    })
    List<Review> findByLectureId(Long lectureId);

    @Insert("""
    INSERT INTO review (lecture_id, mentor_id, writer_id, content, rating,
                        status, report_count, is_deleted, deleted_at, created_at)
    VALUES (#{lectureId}, #{mentorId}, #{writerId}, #{content}, #{rating}, 
            #{status}, #{reportCount}, #{isDeleted}, #{deletedAt}, #{createdAt})
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


    @Select("""
      SELECT COUNT(*) 
        FROM lecture_mentee 
       WHERE lecture_id = #{lectureId}
         AND mentee_id  = #{userId}
    """)
    int countLectureMentee(@Param("lectureId") Long lectureId,
                           @Param("userId")    Long userId);

    default boolean hasAttendedLecture(Long lectureId, Long userId) {
        return countLectureMentee(lectureId, userId) > 0;
    }

    @Select("SELECT writer_id FROM review WHERE review_id = #{reviewId}")
    Long findWriterIdByReviewId(Long reviewId);

    @Select("""
      SELECT AVG(rating)
        FROM review
       WHERE lecture_id = #{lectureId}
         AND status = 'AVAILABLE'
         AND is_deleted = FALSE
    """)
    Double findAverageRatingByLectureId(Long lectureId);

    @Select("""
      SELECT AVG(rating)
        FROM review
       WHERE mentor_id = #{mentorId}
         AND status = 'AVAILABLE'
         AND is_deleted = FALSE
    """)
    Double findAverageRatingByMentorId(Long mentorId);

    @Select("""
        SELECT COUNT(*) 
          FROM review r
         INNER JOIN lecture l
            ON r.lecture_id = l.lecture_id
         WHERE l.mentor_id = #{mentorId}
        """)
    Long countReviewsByMentorId(@Param("mentorId") Long mentorId);

    @Select("""
        SELECT COUNT(*) 
          FROM review
         WHERE lecture_id = #{lectureId}
        """)
    Long countReviewsByLectureId(@Param("lectureId") Long lectureId);

}
