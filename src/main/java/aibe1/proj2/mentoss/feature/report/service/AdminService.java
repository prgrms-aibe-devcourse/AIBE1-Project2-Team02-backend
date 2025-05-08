package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.*;

import java.util.List;

public interface AdminService {
    List<ReportResponseDto> getReportsNotProcessed();

    List<ReportDoneResponseDto> getReportsProcessed();

    void updateStatus(StatusUpdateRequestDto request);

    void softDelete(SoftDeleteRequestDto request);

    void processReport(Long reportId);

    void recover(RecoverRequestDto req);

    Long adminAction(ReportProcessRequestDto req, Long adminId);

    void reportActionRelation(Long reportId, Long actionId);
}
