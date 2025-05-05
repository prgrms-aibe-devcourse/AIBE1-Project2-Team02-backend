package aibe1.proj2.mentoss.feature.account.model.mapper;

import aibe1.proj2.mentoss.global.entity.MentorProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface MentorMapper {

    @Select("SELECT * FROM mentor_profile " +
            "WHERE user_id = #{userId}")
    Optional<MentorProfile> findByUserId(Long userId);

    @Select("SELECT COUNT(*) > 0 FROM mentor_profile " +
            "WHERE user_id = #{userId}")
    boolean existsByUserId(Long userId);
}
