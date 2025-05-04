package aibe1.proj2.mentoss.feature.report.model.mapper;


import aibe1.proj2.mentoss.global.entity.Report;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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

}
