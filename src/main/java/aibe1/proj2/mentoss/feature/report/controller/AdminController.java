package aibe1.proj2.mentoss.feature.report.controller;

import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.model.dto.SoftDeleteRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.StatusUpdateRequestDto;
import aibe1.proj2.mentoss.feature.report.service.AdminService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reports/done")
    public ResponseEntity<ApiResponseFormat<List<ReportResponseDto>>> getProcessedReports() {
        List<ReportResponseDto> list = adminService.getReportsByProcessed(true);
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }

    @GetMapping("/reports/notdone")
    public ResponseEntity<ApiResponseFormat<List<ReportResponseDto>>> getNotProcessedReports() {
        List<ReportResponseDto> list = adminService.getReportsByProcessed(false);
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }


    @PutMapping("/user/{userId}/status")
    public ResponseEntity<ApiResponseFormat<Void>> updateStatus(
            @RequestBody StatusUpdateRequestDto req) {
        adminService.updateStatus(req);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @PutMapping("/{entity}/{id}/soft-delete")
    public ResponseEntity<ApiResponseFormat<Void>> toggleSoftDelete(
            @RequestBody SoftDeleteRequestDto req) {
        adminService.toggleSoftDelete(req);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @PutMapping("/reports/{reportId}/process")
    public ResponseEntity<ApiResponseFormat<Void>> processReport(
            @PathVariable Long reportId) {
        adminService.processReport(reportId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}
