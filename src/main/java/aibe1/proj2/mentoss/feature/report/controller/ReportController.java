package aibe1.proj2.mentoss.feature.report.controller;


import aibe1.proj2.mentoss.feature.report.model.dto.CreateReportRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.ReportResponseDto;
import aibe1.proj2.mentoss.feature.report.service.ReportService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import aibe1.proj2.mentoss.global.entity.Report;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "신고 생성", description = "사용자의 신고 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신고 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                    {
                      "success": true,
                      "message": "신고가 성공적으로 등록되었습니다.",
                      "data": null
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "409", description = "중복 신고(같은 대상에 이미 신고한 경우)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                    {
                      "success": false,
                      "message": "이미 이 대상을 신고하셨습니다.",
                      "data": null
                    }
                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponseFormat<ReportResponseDto>> createReport(
            @RequestBody CreateReportRequestDto dto) {
        reportService.createReport(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFormat.ok(null));
    }

}
