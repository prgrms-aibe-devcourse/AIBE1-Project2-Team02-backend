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
      review_id        AS reviewId,
      lecture_id       AS lectureId,
      mentor_id        AS mentorId,
      writer_id        AS writerId,
      content,
      rating,
      created_at       AS createdAt
    FROM review
    WHERE mentor_id = #{mentorId}
      AND is_deleted = FALSE
    ORDER BY created_at DESC
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
