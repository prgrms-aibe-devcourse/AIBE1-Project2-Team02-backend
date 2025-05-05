package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.model.dto.SoftDeleteRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.StatusUpdateRequestDto;

import java.util.List;

public interface AdminService {
    List<ReportResponseDto> getReportsByProcessed(boolean processed);

    void updateStatus(StatusUpdateRequestDto request);

    void toggleSoftDelete(SoftDeleteRequestDto request);

    void processReport(Long reportId);
}
