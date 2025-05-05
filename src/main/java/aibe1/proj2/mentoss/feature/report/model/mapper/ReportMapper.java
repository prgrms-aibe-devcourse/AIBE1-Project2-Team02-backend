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

}
