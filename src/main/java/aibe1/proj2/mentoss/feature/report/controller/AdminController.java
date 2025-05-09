package aibe1.proj2.mentoss.feature.report.controller;

import aibe1.proj2.mentoss.feature.report.model.dto.*;
import aibe1.proj2.mentoss.feature.report.service.AdminService;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "관리자 API", description = "관리자 페이지 및 기능 API")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "처리 완료된 신고 목록 조회", description = "처리 완료된 모든 신고 내역을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                                    {
                                           "success": true,
                                           "message": "요청이 성공적으로 처리되었습니다.",
                                           "data": [
                                             {
                                               "reportId": 1,
                                               "reporterId": 1,
                                               "targetType": "USER",
                                               "targetId": 1,
                                               "reason": "string",
                                               "reasonType": "WARN",
                                               "processedAt": [
                                                 2025,
                                                 5,
                                                 6,
                                                 11,
                                                 55,
                                                 40
                                               ],
                                               "processAdminId": 1,
                                               "actionType": "WARN",
                                               "actionReason": "string",
                                               "suspendPeriod": 10
                                             }
                                           ]
                                    }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "신고 기록을 가져오는 중 오류 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            )
    })
    @GetMapping("/reports/done")
    public ResponseEntity<ApiResponseFormat<List<ReportDoneResponseDto>>> getProcessedReports() {
        List<ReportDoneResponseDto> list = adminService.getReportsProcessed();
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }


    @Operation(summary = "처리 이전 상태인 신고 목록 조회", description = "처리 안 된 모든 신고 내역을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                                    {
                                          "success": true,
                                          "message": "요청이 성공적으로 처리되었습니다.",
                                          "data": [
                                            {
                                              "reportId": 6,
                                              "reporterId": 1,
                                              "targetType": "REVIEW",
                                              "targetId": 1,
                                              "reason": "후기 내용이 이상해요",
                                              "reasonType": "PROFILE"
                                            }
                                          ]
                                    }
                                """
                                )
                        )
                ),
            @ApiResponse(responseCode = "400", description = "TargetType이 지정된 값 이외의 것이 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
        })
    @GetMapping("/reports/not-done")
    public ResponseEntity<ApiResponseFormat<List<ReportResponseDto>>> getNotProcessedReports() {
        List<ReportResponseDto> list = adminService.getReportsNotProcessed();
        return ResponseEntity.ok(ApiResponseFormat.ok(list));
    }



    @Operation(summary = "특정 항목 상태 변경", description = "USER/LECTURE/REVIEW의 status를 AVAILABLE/SUSPENDED/BANNED로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": true,
                              "message": "요청이 성공적으로 처리되었습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "TargetType이 지정된 값 이외의 것이 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "대상 엔티티를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "Lecture (id=123) 가 존재하지 않습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            )
    })
    @PutMapping("/status")
    public ResponseEntity<ApiResponseFormat<Void>> updateStatus(
            @RequestBody StatusUpdateRequestDto dto) {
        adminService.updateStatus(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "항목 삭제(Soft Delete)", description = "USER/LECTURE/REVIEW를 soft delete 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 처리 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": true,
                              "message": "요청이 성공적으로 처리되었습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "TargetType이 지정된 값 이외의 것이 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "대상 엔티티를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "User (id=45) 가 존재하지 않습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            )
    })
    @PutMapping("/soft-delete")
    public ResponseEntity<ApiResponseFormat<Void>> softDelete(
            @RequestBody SoftDeleteRequestDto dto) {
        adminService.softDelete(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "삭제 항목 복구", description = "USER/LECTURE/REVIEW의 soft delete 상태를 복구합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "복구 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": true,
                              "message": "요청이 성공적으로 처리되었습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "TargetType이 지정된 값 이외의 것이 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "대상 엔티티를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "Review (id=78) 가 존재하지 않습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            )
    })
    @PutMapping("/recover")
    public ResponseEntity<ApiResponseFormat<Void>> recover(
            @RequestBody RecoverRequestDto dto){
        adminService.recover(dto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "신고 처리", description = "신고에 대한 조치 기록을 남기고 신고 처리 상태를 완료로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "처리 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": true,
                              "message": "요청이 성공적으로 처리되었습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "ActionType이 지정된 값 이외의 것이 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "actionType은 FREE, WARN, SUSPEND, BAN 중 하나여야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "제재일 측정 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "Action이 FREE나 WARN일 경우 제재일은 0일이어야 합니다.",
                              "data": null
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "DB / SQL 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseFormat.class, example = """
                            {
                              "success": false,
                              "message": "데이터베이스 처리 중 문제가 발생했습니다.",
                              "data": null
                            }
                            """
                            )
                    )
            )
    })
    @PutMapping("/process")
    public ResponseEntity<ApiResponseFormat<Void>> processReport(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReportProcessRequestDto dto) {
        Long actionId = adminService.adminAction(dto, user.getUserId());
        adminService.reportActionRelation(dto.reportId(), actionId);
        adminService.processReport(dto.reportId());
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}
