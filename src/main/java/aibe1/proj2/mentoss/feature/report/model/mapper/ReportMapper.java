package aibe1.proj2.mentoss.feature.report.model.mapper;


import aibe1.proj2.mentoss.global.entity.Report;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReportMapper {
    @Insert("""
        INSERT INTO report
          (reporter_id,
           target_type,
           target_id,
           reason,
           reason_type,
           is_processed)
        VALUES
          (#{reporterId},
           #{targetType},
           #{targetId},
           #{reason},
           #{reasonType},
           #{isProcessed})
        """)
    void insertReport(Report report);

    @Select("""
      SELECT COUNT(*) 
        FROM report
       WHERE reporter_id = #{reporterId}
         AND target_type  = #{targetType}
         AND target_id    = #{targetId}
    """)
    int countByReporterAndTarget(@Param("reporterId") Long reporterId,
                                 @Param("targetType")  String targetType,
                                 @Param("targetId")    Long targetId);

    @Select("SELECT COUNT(*) FROM app_user WHERE user_id = #{userId}")
    int countUserById(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM lecture WHERE lecture_id = #{lectureId}")
    int countLectureById(@Param("lectureId") Long lectureId);

    @Select("SELECT COUNT(*) FROM review WHERE review_id = #{reviewId}")
    int countReviewById(@Param("reviewId") Long reviewId);

    @Update("UPDATE app_user   SET report_count = report_count + 1 WHERE user_id    = #{userId}")
    void incrementUserReportCount(@Param("userId") Long userId);

    @Update("UPDATE lecture    SET report_count = report_count + 1 WHERE lecture_id = #{lectureId}")
    void incrementLectureReportCount(@Param("lectureId") Long lectureId);

    @Update("UPDATE review     SET report_count = report_count + 1 WHERE review_id  = #{reviewId}")
    void incrementReviewReportCount(@Param("reviewId") Long reviewId);

    @Select("SELECT report_count FROM app_user   WHERE user_id    = #{userId}")
    int getUserReportCount(@Param("userId") Long userId);

    @Update("UPDATE app_user   SET status = 'SUSPENDED' WHERE user_id    = #{userId}")
    void suspendUser(@Param("userId") Long userId);

    @Select("SELECT report_count FROM lecture    WHERE lecture_id = #{lectureId}")
    int getLectureReportCount(@Param("lectureId") Long lectureId);

    @Update("UPDATE lecture    SET status = 'SUSPENDED' WHERE lecture_id = #{lectureId}")
    void suspendLecture(@Param("lectureId") Long lectureId);

    @Select("SELECT report_count FROM review     WHERE review_id  = #{reviewId}")
    int getReviewReportCount(@Param("reviewId") Long reviewId);

    @Update("UPDATE review     SET status = 'SUSPENDED' WHERE review_id  = #{reviewId}")
    void suspendReview(@Param("reviewId") Long reviewId);
}
