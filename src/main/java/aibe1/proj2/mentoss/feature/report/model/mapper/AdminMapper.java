package aibe1.proj2.mentoss.feature.report.model.mapper;


import aibe1.proj2.mentoss.feature.report.model.dto.ReportDoneResponseDto;
import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminMapper {
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
            aa.admin_id       AS processAdminId
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




}
