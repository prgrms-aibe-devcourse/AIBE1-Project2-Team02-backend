package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.model.dto.SoftDeleteRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.StatusUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ReportService reportService;


    @Override
    public List<ReportResponseDto> getReportsByProcessed(boolean processed) {
        return List.of();
    }

    @Override
    public void updateStatus(StatusUpdateRequestDto request) {

    }

    @Override
    public void toggleSoftDelete(SoftDeleteRequestDto request) {

    }

    @Override
    public void processReport(Long reportId) {

    }
}
