package aibe1.proj2.mentoss.feature.application.model.mapper;

import aibe1.proj2.mentoss.feature.application.model.dto.*;
import aibe1.proj2.mentoss.global.entity.Application;
import aibe1.proj2.mentoss.global.entity.Lecture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface ApplicationMapper {

    // 내가 신청한 과외 목록
    @Select("""
                SELECT
                    a.application_id,
                    a.lecture_id,
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
                    AND status = 'AVAILABLE'
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
                    a.application_id DESC
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
                    u.nickname,
                    u.profile_image,
                    mp.is_certified,
                    l.is_closed
                FROM lecture l
                LEFT JOIN lecture_category lc ON l.category_id = lc.category_id
                                LEFT JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id
                                LEFT JOIN app_user u ON mp.user_id = u.user_id
                LEFT JOIN (
                    SELECT lecture_id, ROUND(AVG(rating), 1) AS average_rating
                    FROM review
                    WHERE is_deleted = 0
                    AND status = 'AVAILABLE'
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
                    u.user_id = #{mentorId}
                    AND l.is_deleted = 0
                    AND l.status = 'AVAILABLE'
                ORDER BY 
                    l.lecture_id DESC
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
                JOIN mentor_profile mp on l.mentor_id = mp.mentor_id
                WHERE
                    mp.user_id = #{userId}
                    AND a.status = 'PENDING'
                    AND a.is_deleted = 0
                ORDER BY
                    a.application_id
            """)
    List<LectureApplicantDto> findApplicantsByLecture(Long userId);

    // 수락 처리
    @Update("""
                UPDATE application
                SET status = 'APPROVED', updated_at = #{LocalTime}
                WHERE application_id = #{applicationId}
            """)
    int acceptApplication(Long applicationId, LocalDateTime LocalTime);

    @Insert("""
            INSERT INTO lecture_mentee(lecture_id, mentee_id, matched_price, matched_time_slots, joined_at)
            SELECT a.lecture_id, a.mentee_id, l.price, a.requested_time_slots, #{LocalTime}
            FROM application a
            JOIN lecture l on a.lecture_id = l.lecture_id
            WHERE a.application_id = #{applicationId}
            """)
    void insertLectureMentee(Long applicationId, LocalDateTime LocalTime);


    // 반려 처리
    @Update("""
                UPDATE application
                SET status = 'REJECTED', updated_at = #{LocalTime}
                WHERE application_id = #{applicationId}
            """)
    void rejectApplication(Long applicationId, LocalDateTime LocalTime);

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


    @Select("""
            SELECT l.lecture_id, mp.user_id , l.is_closed, l.status
            FROM lecture l
            JOIN mentor_profile mp on l.mentor_id = mp.mentor_id
            WHERE l.lecture_id = #{lectureId}
    """)
    LectureStatusDto findLectureStatusById(Long lectureId);

    @Select("""
            SELECT COUNT(*)
            FROM application
            WHERE lecture_id = #{lectureId}
              AND status = 'PENDING'
              AND is_deleted = 0
    """)
    int countPendingApplicationsByLectureId(Long lectureId);
    @Update("""
            UPDATE lecture
            SET is_closed = #{isClosed}
            WHERE lecture_id = #{lectureId}
    """)
    void updateLectureStatus(Long lectureId, boolean isClosed);

    @Select("""
                SELECT
                    l.lecture_id,
                    l.lecture_title,
                    l.available_time_slots,
                    u.profile_image,
                    u.nickname,
                    mp.education,
                    mp.major,
                    mp.is_certified,
                    COALESCE(rv_avg.average_rating, 0) AS average_rating
                FROM lecture l
                JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id
                JOIN app_user u ON mp.user_id = u.user_id
                LEFT JOIN (
                    SELECT mentor_id, ROUND(AVG(rating), 1) AS average_rating
                    FROM review
                    WHERE is_deleted = 0
                    AND status = 'AVAILABLE'
                    GROUP BY mentor_id
                ) rv_avg ON rv_avg.mentor_id = l.mentor_id
                WHERE
                    l.lecture_id = #{lectureId}
                    AND l.is_deleted = 0
                    AND l.status = 'AVAILABLE'
            """)
    LectureApplyFormRawDto findLectureApplyFormData(Long lectureId);

    @Insert("""
                INSERT INTO application (lecture_id, mentee_id, requested_time_slots, status, is_deleted, created_at, updated_at) 
                VALUES (#{lectureId}, #{menteeId}, #{requestedTimeSlots},'PENDING',0, #{localTime}, #{localTime})
            """)
    void insertApplication(Long lectureId, Long menteeId, String requestedTimeSlots, LocalDateTime localTime);

    @Select("""
                SELECT l.lecture_title, mp.user_id
                FROM lecture l
                LEFT JOIN mentor_profile mp on l.mentor_id = mp.mentor_id
                WHERE l.lecture_id = #{lectureId}
            """)
    LectureSimpleInfoDto findLectureSimpleInfo(Long lectureId);

    @Select("""
            SELECT COUNT(*)
            FROM application
            WHERE lecture_id = #{lectureId}
              AND mentee_id = #{menteeId}
              AND status = 'PENDING'
            """)
    int countDuplicateApplication(Long lectureId, Long menteeId);
}
