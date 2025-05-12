package aibe1.proj2.mentoss.feature.review.model.mapper;

import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.global.entity.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Select("""
    SELECT
      r.review_id       AS reviewId,
      r.lecture_id      AS lectureId,
      r.mentor_id       AS mentorId,
      r.writer_id       AS writerId,
      u.nickname        AS writerNickname,
      u.profile_image   AS writerProfileImage,
      r.content,
      r.rating,
      r.created_at      AS createdAt
      r.updated_at      AS updatedAt
    FROM review r
    JOIN app_user u ON u.user_id = r.writer_id
    WHERE r.lecture_id = #{lectureId}
      AND r.is_deleted = FALSE
    ORDER BY r.created_at DESC
  """)
    @ConstructorArgs({
            @Arg(column="reviewId",            javaType=Long.class,            id=true),
            @Arg(column="lectureId",           javaType=Long.class),
            @Arg(column="mentorId",            javaType=Long.class),
            @Arg(column="writerId",            javaType=Long.class),
            @Arg(column="writerNickname",      javaType=String.class),
            @Arg(column="writerProfileImage",  javaType=String.class),
            @Arg(column="content",             javaType=String.class),
            @Arg(column="rating",              javaType=Long.class),
            @Arg(column="createdAt",           javaType= LocalDateTime.class),
            @Arg(column="updatedAt",           javaType= LocalDateTime.class)
    })
    List<ReviewResponseDto> findByLectureId(Long lectureId);

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
      SELECT AVG(r.rating)
        FROM review r
       INNER JOIN lecture l
          ON r.lecture_id = l.lecture_id
       WHERE l.mentor_id = #{mentorId}
         AND r.status = 'AVAILABLE'
         AND r.is_deleted = FALSE
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
