package aibe1.proj2.mentoss.feature.report.controller;

import aibe1.proj2.mentoss.feature.report.model.dto.*;
import aibe1.proj2.mentoss.feature.report.service.AdminService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "관리자 API", description = "관리자 페이지 및 기능 API")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reports/done")
    public ResponseEntity<ApiResponseFormat<List<ReportDoneResponseDto>>> getProcessedReports() {
        List<ReportDoneResponseDto> list = adminService.getReportsProcessed();
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }

    @GetMapping("/reports/not-done")
    public ResponseEntity<ApiResponseFormat<List<ReportResponseDto>>> getNotProcessedReports() {
        List<ReportResponseDto> list = adminService.getReportsNotProcessed();
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }


    @PutMapping("/status")
    public ResponseEntity<ApiResponseFormat<Void>> updateStatus(
            @RequestBody StatusUpdateRequestDto dto) {
        adminService.updateStatus(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @PutMapping("/soft-delete")
    public ResponseEntity<ApiResponseFormat<Void>> softDelete(
            @RequestBody SoftDeleteRequestDto dto) {
        adminService.softDelete(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @PutMapping("/recover")
    public ResponseEntity<ApiResponseFormat<Void>> recover(
            @RequestBody RecoverRequestDto dto){
        adminService.recover(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @PutMapping("/process")
    public ResponseEntity<ApiResponseFormat<Void>> processReport(
            @RequestBody ReportProcessRequestDto dto) {
        Long actionId = adminService.adminAction(dto);
        adminService.reportActionRelation(dto.reportId(), actionId);
        adminService.processReport(dto.reportId());
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}
