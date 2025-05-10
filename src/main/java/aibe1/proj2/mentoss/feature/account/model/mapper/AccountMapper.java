package aibe1.proj2.mentoss.feature.account.model.mapper;

import aibe1.proj2.mentoss.global.entity.AppUser;
import aibe1.proj2.mentoss.global.entity.Region;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface AccountMapper {

    @Select("SELECT * FROM app_user " +
            "WHERE user_id = #{userId} AND is_deleted = FALSE")
    Optional<AppUser> findByUserId(Long userId);

    @Select("SELECT r.* FROM region r " +
            "JOIN user_region ur ON r.region_code = ur.region_code " +
            "WHERE ur.user_id = #{userId}")
    List<Region> findRegionByUserId(Long userId);

    @Select("SELECT * FROM region " +
            "WHERE region_code = #{regionCode}")
    Optional<Region> findRegionByRegionCode(String regionCode);

    @Update("UPDATE app_user SET " +
            "nickname = #{nickname}, " +
            "birth_date = #{birthDate}, " +
            "age = #{age}, " +
            "sex = #{sex}, " +
            "region_code = #{regionCode}, " +
            "mbti = #{mbti}, " +
            "updated_at = NOW() " +
            "WHERE user_id = #{userId}")
    int updateProfile(AppUser appUser);

    @Delete("DELETE FROM user_region " +
            "WHERE user_id = #{userId}" )
    void deleteUserRegion(Long userId);

    @Insert("INSERT INTO user_region (user_id, region_code, created_at) " +
            "VALUES (#{userId}, #{regionCode}, NOW())")
    void insertUserRegion(@Param("userId") Long userId, @Param("regionCode") String regionCode);

    @Select("SELECT COUNT(*) > 0 FROM app_user " +
            "WHERE user_id = #{userId} " +
            "AND nickname IS NOT NULL " +
            "AND birth_date IS NOT NULL " +
            "AND sex IS NOT NULL")
    boolean isProfileCompleted(Long userId);


    @Update("UPDATE app_user SET " +
            "is_deleted = #{isDeleted}, " +
            "deleted_at = #{deletedAt} " +
            "WHERE user_id = #{userId}")
    void softDeleteUser(AppUser appUser);

    @Update("UPDATE app_user SET " +
            "profile_image = #{profileImage}, " +
            "updated_at = NOW() " +
            "WHERE user_id = #{userId}")
    void updateProfileImage(@Param("userId") Long userId, @Param("profileImage") String profileImage);

    /**
     * 탈퇴 회원 익명화 처리
     */
    @Update("UPDATE app_user SET " +
            "is_deleted = #{isDeleted}, " +
            "deleted_at = #{deletedAt}, " +
            "nickname = #{nickname}, " +
            "email = #{email}, " +
            "birth_date = #{birthDate}, " +
            "age = #{age}, " +
            "sex = #{sex}, " +
            "profile_image = #{profileImage}, " +
            "mbti = #{mbti}, " +
            "updated_at = NOW() " +
            "WHERE user_id = #{userId}")
    int anonymizeDeletedUser(AppUser appUser);
}
