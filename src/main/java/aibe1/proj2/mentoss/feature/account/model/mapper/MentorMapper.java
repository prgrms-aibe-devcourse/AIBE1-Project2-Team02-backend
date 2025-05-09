package aibe1.proj2.mentoss.feature.account.model.mapper;

import aibe1.proj2.mentoss.global.entity.MentorProfile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface MentorMapper {

    @Select("SELECT mp.* FROM mentor_profile mp " +
            "JOIN app_user u ON mp.user_id = u.user_id " +
            "WHERE mp.user_id = #{userId} " +
            "AND u.is_deleted = FALSE")
    Optional<MentorProfile> findByUserId(Long userId);

    @Select("SELECT COUNT(*) > 0 FROM mentor_profile mp " +
            "JOIN app_user u ON mp.user_id = u.user_id " +
            "WHERE mp.user_id = #{userId} " +
            "AND u.is_deleted = FALSE")
    boolean existsByUserId(Long userId);

    @Insert("INSERT INTO mentor_profile " +
            "(user_id, content, appeal_file_url, is_certified, created_at) " +
            "VALUES (#{userId}, #{content}, #{appealFileUrl}, #{isCertified}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "mentorId")
    int insertMentorProfile(MentorProfile mentorProfile);

    @Update("UPDATE app_user SET role = 'MENTOR' " +
            "WHERE user_id = #{userId}")
    int updateToMentorRole(Long userId);


    @Update("UPDATE mentor_profile SET " +
            "content = #{content}, " +
            "appeal_file_url = #{appealFileUrl} " +
            "WHERE user_id = #{userId}")
    int updateMentorProfile(MentorProfile mentorProfile);

    @Select("SELECT COUNT(*) > 0 FROM mentor_profile " +
            "WHERE user_id = #{userId}")
    boolean existsByUserIdWithoutDeleteCheck(Long userId);

    @Select("SELECT mentor_id FROM mentor_profile " +
            "WHERE user_id = #{userId}")
    Long findMentorIdByUserId(Long userId);

    @Update("UPDATE mentor_profile SET " +
            "is_certified = is_certified " + // 자기 자신을 할당 (변화 없음)
            "WHERE user_id = #{userId}")
    int softDeleteMentorProfile(Long userId);
}
