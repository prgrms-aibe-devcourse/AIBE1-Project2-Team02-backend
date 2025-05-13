package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.request.CreateReportRequestDto;

public interface ReportService {
    void createReport(CreateReportRequestDto request, Long reporterId);
}
