package aibe1.proj2.mentoss.feature.report.controller;

import aibe1.proj2.mentoss.feature.report.model.dto.RecoverRequestDto;
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

    @GetMapping("/reports/not-done")
    public ResponseEntity<ApiResponseFormat<List<ReportResponseDto>>> getNotProcessedReports() {
        List<ReportResponseDto> list = adminService.getReportsByProcessed(false);
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }


    @PutMapping("/status")
    public ResponseEntity<ApiResponseFormat<Void>> updateStatus(
            @RequestBody StatusUpdateRequestDto req) {
        adminService.updateStatus(req);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @PutMapping("/soft-delete")
    public ResponseEntity<ApiResponseFormat<Void>> softDelete(
            @RequestBody SoftDeleteRequestDto req) {
        adminService.softDelete(req);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @PutMapping("/recover")
    public ResponseEntity<ApiResponseFormat<Void>> recover(
            @RequestBody RecoverRequestDto req){
        adminService.recover(req);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @PutMapping("/reports/{reportId}/process")
    public ResponseEntity<ApiResponseFormat<Void>> processReport(
            @PathVariable Long reportId) {
        adminService.processReport(reportId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}
