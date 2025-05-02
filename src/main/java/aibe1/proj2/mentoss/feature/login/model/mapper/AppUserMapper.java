package aibe1.proj2.mentoss.feature.login.model.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface AppUserMapper {

    @Select("SELECT * FROM app_user WHERE provider = #{provider} AND provider_id = #{providerId}")
    Optional<AppUser> findByProviderAndProviderId(String provider, String providerId);

    @Select("SELECT * FROM app_user WHERE email = #{email}")
    Optional<AppUser> findByEmail(String email);

    @Insert("INSERT INTO app_user (region_code, provider, provider_id, email, nickname, profile_image, role, status, report_count, is_deleted, created_at, updated_at) " +
            "VALUES (#{regionCode}, #{provider}, #{providerId}, #{email}, #{nickname}, #{profileImage}, #{role}, #{status}, #{reportCount}, #{isDeleted}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int save(AppUser appUser);

    @Update("UPDATE app_user SET " +
            "nickname = #{nickname}, " +
            "region_code = #{regionCode}, " +
            "age = #{age}, " +
            "sex = #{sex}, " +
            "mbti = #{mbti}, " +
            "updated_at = NOW() " +
            "WHERE user_id = #{userId}")
    int update(AppUser appUser);

}
