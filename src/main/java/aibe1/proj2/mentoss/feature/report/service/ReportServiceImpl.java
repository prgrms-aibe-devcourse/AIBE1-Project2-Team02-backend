package aibe1.proj2.mentoss.feature.report.service;


import aibe1.proj2.mentoss.feature.report.model.dto.CreateReportRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.model.mapper.ReportMapper;
import aibe1.proj2.mentoss.global.entity.Report;
import aibe1.proj2.mentoss.global.exception.DuplicateReportException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;

    @Override
    public void createReport(CreateReportRequestDto req) {
        int count = reportMapper.countByReporterAndTarget(
                req.reporterId(), req.targetType(), req.targetId()
        );
        if (count > 0) {
            throw new DuplicateReportException();
        }
        Report report = Report.builder()
                .reporterId(req.reporterId())
                .targetType(req.targetType())
                .targetId(req.targetId())
                .reason(req.reason())
                .reasonType(req.reasonType())
                .isProcessed(false)
                .build();
        reportMapper.insertReport(report);
    }

}
