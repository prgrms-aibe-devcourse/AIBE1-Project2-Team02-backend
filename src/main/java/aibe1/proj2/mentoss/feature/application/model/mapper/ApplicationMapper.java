package aibe1.proj2.mentoss.feature.application.model.mapper;

import aibe1.proj2.mentoss.feature.application.model.dto.ApplicationInfoDto;
import aibe1.proj2.mentoss.feature.application.model.dto.AppliedLectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureApplicantDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureResponseDto;
import aibe1.proj2.mentoss.global.entity.Application;
import aibe1.proj2.mentoss.global.entity.Lecture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ApplicationMapper {

    // 내가 신청한 과외 목록
    @Select("""
                SELECT
                    a.application_id,
                    a.status,
                    l.lecture_title,
                    l.price,
                    u.nickname,
                    mp.is_certified,
                    COALESCE(rv_avg.average_rating, 0) AS average_rating,
                    rg.preferred_regions,
                    lc.subcategory,
                    u.profile_image
                FROM application a
                LEFT JOIN lecture l ON a.lecture_id = l.lecture_id
                LEFT JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id
                LEFT JOIN app_user u ON mp.user_id = u.user_id
                LEFT JOIN lecture_category lc ON l.category_id = lc.category_id
                LEFT JOIN (
                    SELECT lecture_id, ROUND(AVG(rating), 1) AS average_rating
                    FROM review
                    WHERE is_deleted = 0
                    GROUP BY lecture_id
                ) rv_avg ON rv_avg.lecture_id = l.lecture_id
                LEFT JOIN (
                    SELECT
                        lr.lecture_id,
                        GROUP_CONCAT(DISTINCT r.sigungu ORDER BY r.sigungu SEPARATOR ', ') AS preferred_regions
                    FROM lecture_region lr
                    JOIN region r ON lr.region_code = r.region_code
                    GROUP BY lr.lecture_id
                ) rg ON rg.lecture_id = l.lecture_id
                WHERE
                    a.mentee_id = #{menteeId}
                    AND a.is_deleted = 0
                    AND l.is_deleted = 0
                ORDER BY
                    a.created_at DESC
            """)
    List<AppliedLectureResponseDto> findMyAppliedLectures(Long menteeId);

    // 내가 등록한 과외 목록
    @Select("""
                SELECT 
                    l.lecture_id,
                    l.lecture_title,
                    l.price,
                    COALESCE(rv_avg.average_rating, 0) AS average_rating,
                    rg.preferred_regions,
                    lc.subcategory,
                    u.profile_image
                FROM lecture l
                LEFT JOIN lecture_category lc ON l.category_id = lc.category_id
                LEFT JOIN app_user u ON l.mentor_id = u.user_id
                LEFT JOIN (
                    SELECT lecture_id, ROUND(AVG(rating), 1) AS average_rating
                    FROM review
                    WHERE is_deleted = 0
                    GROUP BY lecture_id
                ) rv_avg ON rv_avg.lecture_id = l.lecture_id
                LEFT JOIN (
                    SELECT 
                        lr.lecture_id,
                        GROUP_CONCAT(DISTINCT r.sigungu ORDER BY r.sigungu SEPARATOR ', ') AS preferred_regions
                    FROM lecture_region lr
                    JOIN region r ON lr.region_code = r.region_code
                    GROUP BY lr.lecture_id
                ) rg ON rg.lecture_id = l.lecture_id
                WHERE 
                    l.mentor_id = #{mentorId}
                    AND l.is_deleted = 0
                ORDER BY 
                    l.created_at DESC
            """)
    List<LectureResponseDto> findLecturesByMentorId(Long mentorId);

    // 특정 강의에 대한 신청 리스트
    @Select("""
        SELECT
            a.application_id,
            u.nickname,
            l.lecture_title,
            DATE(a.created_at) AS created_at,
            u.profile_image
        FROM application a
        JOIN app_user u ON a.mentee_id = u.user_id
        JOIN lecture l ON a.lecture_id = l.lecture_id
        WHERE
            l.lecture_id = #{lectureId}
            AND a.status = 'PENDING'
            AND a.is_deleted = 0
        ORDER BY
            a.created_at DESC
    """)
    List<LectureApplicantDto> findApplicantsByLectureId(Long lectureId);

    // 수락 처리
    @Update("""
        UPDATE application
        SET status = 'ACCEPTED', updated_at = CURRENT_TIMESTAMP
        WHERE application_id = #{applicationId}
    """)
    void acceptApplication(Long applicationId);

    // 반려 처리
    @Update("""
        UPDATE application
        SET status = 'REJECTED', updated_at = CURRENT_TIMESTAMP
        WHERE application_id = #{applicationId}
    """)
    void rejectApplication(Long applicationId);

    @Select("""
                SELECT 
                    a.mentee_id,
                    l.lecture_title
                FROM application a
                JOIN lecture l ON a.lecture_id = l.lecture_id
                WHERE a.application_id = #{applicationId}
            """)
    ApplicationInfoDto findApplicationInfo(Long applicationId);


    @Select("""
                SELECT status
                FROM application
                WHERE application_id = #{applicationId}
            """)
    String findStatusByApplicationId(Long applicationId);
}
