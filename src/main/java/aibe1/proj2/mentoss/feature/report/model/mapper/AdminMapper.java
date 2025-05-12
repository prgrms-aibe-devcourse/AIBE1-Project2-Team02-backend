package aibe1.proj2.mentoss.feature.report.model.mapper;


import aibe1.proj2.mentoss.feature.report.model.dto.response.*;
import aibe1.proj2.mentoss.feature.report.model.dto.ReportTargetDto;
import aibe1.proj2.mentoss.global.entity.AdminAction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminMapper {
    @Select("""
        SELECT
            user_id          AS userId,
            email            AS email,
            nickname         AS nickname,
            age              AS age,
            birth_date       AS birthDate,
            sex              AS sex,
            profile_image    AS profileImage,
            mbti             AS mbti,
            role             AS role,
            status           AS status,
            report_count     AS reportCount,
            is_deleted       AS isDeleted,
            created_at       AS createdAt
        FROM app_user
        WHERE user_id = #{userId}
    """)
    UserDataResponseDto findUserById(@Param("userId") Long userId);

    @Select("""
        SELECT
            l.lecture_id               AS lectureId,
            l.mentor_id                AS mentorId,
            CONCAT_WS(' > ', lc.parent_category, lc.middle_category, lc.subcategory) AS category,
            l.curriculum               AS curriculum,
            l.price                    AS price,
            l.available_time_slots     AS availableTimeSlots,
            l.lecture_title            AS lectureTitle,
            l.description              AS description,
            l.is_closed                AS isClosed,
            l.status                   AS status,
            l.report_count             AS reportCount,
            l.is_deleted               AS isDeleted,
            l.created_at               AS createdAt
        FROM lecture l
        JOIN lecture_category lc ON l.category_id = lc.category_id
        WHERE l.lecture_id = #{lectureId}
    """)
    LectureDataResponseDto findLectureById(@Param("lectureId") Long lectureId);

    @Select("""
        SELECT
            mentor_id       AS mentorId,
            user_id         AS userId,
            education       AS education,
            major           AS major,
            is_certified    AS isCertified,
            content         AS content,
            appeal_file_url AS appealFileUrl,
            tag             AS tag,
            created_at      AS createdAt
        FROM mentor_profile
        WHERE mentor_id = #{mentorId}
    """)
    MentorDataResponseDto findMentorById(@Param("mentorId") Long mentorId);

    @Select("""
        SELECT
            message_id  AS messageId,
            sender_id   AS senderId,
            receiver_id AS receiverId,
            content     AS content,
            is_read     AS isRead,
            is_deleted  AS isDeleted,
            created_at  AS createdAt
        FROM message
        WHERE message_id = #{messageId}
    """)
    MessageDataResponseDto findMessageById(@Param("messageId") Long messageId);

    @Select("""
        SELECT
            application_id       AS applicationId,
            lecture_id           AS lectureId,
            mentee_id            AS menteeId,
            requested_time_slots AS requestedTimeSlots,
            status               AS status,
            is_deleted           AS isDeleted,
            created_at           AS createdAt
        FROM application
        WHERE application_id = #{applicationId}
    """)
    ApplicationDataResponseDto findApplicationById(@Param("applicationId") Long applicationId);

    @Select("""
        SELECT
            review_id    AS reviewId,
            lecture_id   AS lectureId,
            mentor_id    AS mentorId,
            writer_id    AS writerId,
            content      AS content,
            rating       AS rating,
            status       AS status,
            report_count AS reportCount,
            is_deleted   AS isDeleted,
            created_at   AS createdAt
        FROM review
        WHERE review_id = #{reviewId}
    """)
    ReviewDataResponseDto findReviewById(@Param("reviewId") Long reviewId);

    @Select("""
        SELECT
            report_id      AS reportId,
            reporter_id    AS reporterId,
            target_type    AS targetType,
            target_id      AS targetId,
            reason,
            reason_type    AS reasonType
        FROM report
        WHERE is_processed = FALSE
    """
    )
    List<ReportResponseDto> findReportsNotProcessed();

    @Select("""
        SELECT
            r.report_id       AS reportId,
            r.reporter_id     AS reporterId,
            r.target_type     AS targetType,
            r.target_id       AS targetId,
            r.reason,
            r.reason_type     AS reasonType,
            ra.created_at     AS processedAt,
            aa.admin_id       AS processAdminId,
            aa.action_type     AS actionType,
            aa.reason         AS actionReason,
            aa.suspension_period_days AS suspendPeriod
        FROM report r
        JOIN report_action ra ON ra.report_id = r.report_id
        JOIN admin_action  aa ON aa.action_id  = ra.action_id
        WHERE r.is_processed = TRUE
    """)
    List<ReportDoneResponseDto> findReportsProcessed();


    @Update("UPDATE app_user SET status = #{status} WHERE user_id = #{targetId}")
    void updateUserStatus(@Param("targetId") Long targetId,
                          @Param("status") String status);


    @Update("UPDATE lecture SET status = #{status} WHERE lecture_id = #{targetId}")
    void updateLectureStatus(@Param("targetId") Long targetId,
                             @Param("status") String status);


    @Update("UPDATE review SET status = #{status} WHERE review_id = #{targetId}")
    void updateReviewStatus(@Param("targetId") Long targetId,
                            @Param("status") String status);


    @Update("""
        UPDATE app_user
        SET is_deleted = #{isDeleted},
            deleted_at = CASE WHEN #{isDeleted} THEN CURRENT_TIMESTAMP ELSE NULL END
        WHERE user_id = #{targetId}
    """
    )
    void updateUserSoftDelete(@Param("targetId") Long targetId,
                              @Param("isDeleted") boolean isDeleted);


    @Update("""
        UPDATE lecture
        SET is_deleted = #{isDeleted},
            deleted_at = CASE WHEN #{isDeleted} THEN CURRENT_TIMESTAMP ELSE NULL END
        WHERE lecture_id = #{targetId}
    """
    )
    void updateLectureSoftDelete(@Param("targetId") Long targetId,
                                 @Param("isDeleted") boolean isDeleted);

    @Update("""
        UPDATE review
        SET is_deleted = #{isDeleted},
            deleted_at = CASE WHEN #{isDeleted} THEN CURRENT_TIMESTAMP ELSE NULL END
        WHERE review_id = #{targetId}
    """
    )
    void updateReviewSoftDelete(@Param("targetId") Long targetId,
                                @Param("isDeleted") boolean isDeleted);

    @Update("UPDATE report SET is_processed = TRUE WHERE report_id = #{reportId}")
    void markReportProcessed(@Param("reportId") Long reportId);

    @Select("""
        SELECT target_type AS targetType,
               target_id   AS targetId
          FROM report
         WHERE report_id = #{reportId}
    """
    )
    ReportTargetDto findReportTarget(@Param("reportId") Long reportId);

    @Options(useGeneratedKeys = true, keyProperty = "actionId")
    @Insert("""
        INSERT INTO admin_action (
            admin_id, target_type, target_id,
            action_type, reason, suspension_period_days
        ) VALUES (
            #{adminId}, #{targetType}, #{targetId},
            #{actionType}, #{reason}, #{suspensionPeriodDays}
        )
    """
    )
    void insertAdminAction(AdminAction adminAction);

    @Insert("INSERT INTO report_action (report_id, action_id) VALUES (#{reportId}, #{actionId})")
    void insertReportAction(@Param("reportId") Long reportId,
                            @Param("actionId") Long actionId);


}
