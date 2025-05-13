package aibe1.proj2.mentoss.feature.review.model.mapper;


import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.global.entity.MentorProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AiMapper {
    @Select("""
    SELECT
      r.review_id          AS reviewId,
      r.lecture_id         AS lectureId,
      r.mentor_id          AS mentorId,
      r.writer_id          AS writerId,
      u.nickname           AS writerNickname,       
      u.profile_image      AS writerProfileImage,   
      r.content            AS content,              
      r.rating             AS rating,               
      r.created_at         AS createdAt,            
      r.updated_at         AS updatedAt             
    FROM review r
    JOIN app_user u ON r.writer_id = u.user_id
    WHERE r.mentor_id = #{mentorId}
      AND r.is_deleted = FALSE
    ORDER BY r.created_at DESC
    LIMIT 10
""")
    List<ReviewResponseDto> selectReviewsByMentorId(@Param("mentorId") Long mentorId);


    @Update("""
        UPDATE mentor_profile
        SET tag = #{tag}
        WHERE mentor_id = #{mentorId}
    """)
    void updateMentorTag(@Param("mentorId") Long mentorId,
                   @Param("tag") String tag);

    @Select("""
        SELECT *
        FROM mentor_profile
    """)
    List<MentorProfile> findAllMentor();
}
