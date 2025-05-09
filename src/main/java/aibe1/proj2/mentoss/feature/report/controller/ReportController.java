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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@Tag(name = "신고 API", description = "신고 기능 API")
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
            @ApiResponse(responseCode = "400", description = "targetType에 올바른 값이 들어가는지에 대한 검증",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                    {
                      "success": false,
                      "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                      "data": null
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "403", description = "신고 대상 접근 불가(해당 대상이 이미 삭제되었거나 정지 상태)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=10) 항목이 삭제되었거나 정지 상태입니다.",
            "data": null
            }
            """))),
            @ApiResponse(responseCode = "404", description = "신고할 대상이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
            {
            "success": false,
            "message": "Review (id=1234) 가 존재하지 않습니다.",
            "data": null
            }
            """))),
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
