package aibe1.proj2.mentoss.feature.report.service;


import aibe1.proj2.mentoss.feature.report.model.dto.CreateReportRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.model.mapper.ReportMapper;
import aibe1.proj2.mentoss.global.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;

    @Override
    public ReportResponseDto createReport(CreateReportRequestDto req) {
        Report report = new Report();
        reportMapper.insertReport(report);
    }

}
