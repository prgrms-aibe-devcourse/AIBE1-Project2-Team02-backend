package aibe1.proj2.mentoss.feature.application.controller;

import aibe1.proj2.mentoss.feature.application.model.dto.*;
import aibe1.proj2.mentoss.feature.application.service.ApplicationService;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(
            summary = "내가 신청한 과외 목록 조회",
            description = "현재 로그인한 사용자가 신청한 과외 목록을 조회합니다."
    )
    @GetMapping("/apply/list")
    public ResponseEntity<ApiResponseFormat<List<AppliedLectureResponseDto>>> getMyAppliedLectures(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<AppliedLectureResponseDto> result = applicationService.getMyAppliedLectures(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "내가 등록한 과외 목록 조회",
            description = "현재 로그인한 사용자가 등록한 과외 목록을 조회합니다."
    )
    @GetMapping("/apply/lectures/list")
    public ResponseEntity<ApiResponseFormat<List<LectureResponseDto>>> getLecturesByMentor(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<LectureResponseDto> result = applicationService.getLecturesByMentor(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "신청한 멘티 리스트 조회",
            description = "멘토한테 신청된 전체 과외 목록을 조회합니다."
    )
    @GetMapping("/applicants")
    public ResponseEntity<ApiResponseFormat<List<LectureApplicantDto>>> getLectureApplicants(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<LectureApplicantDto> applicants = applicationService.getApplicantsByLecture(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(applicants));
    }

    @Operation(summary = "과외 신청 수락", description = "applicationId를 받아 해당 신청을 수락 처리합니다.")
    @PostMapping("/approve")
    public ResponseEntity<ApiResponseFormat<Void>> approveApplication(
            @RequestBody ApproveApplicationRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        applicationService.approveApplication(dto.applicationId(), userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "과외 신청 반려", description = "applicationId와 반려 사유를 받아 신청을 반려 처리합니다.")
    @PostMapping("/reject")
    public ResponseEntity<ApiResponseFormat<Void>> rejectApplication(
            @RequestBody RejectApplicationRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        applicationService.rejectApplication(dto.applicationId(), dto.reason(), userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "강의 상태 변경", description = "강의 마감 여부 상태를 변경합니다.")
    @PatchMapping("/lecture/status/{lectureId}")
    public ResponseEntity<ApiResponseFormat<String>> updateLectureStatus(
            @PathVariable Long lectureId,
            @RequestBody LectureStatusUpdateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        applicationService.updateLectureStatus(lectureId, request.isClosed(), userDetails.getUserId());
        return ResponseEntity.ok(ApiResponseFormat.ok("강의 상태가 변경되었습니다."));
    }

    @Operation(summary = "과외 신청 폼 데이터 조회", description = "과외 ID를 통해 신청 폼에 필요한 정보를 조회합니다.")
    @GetMapping("/{lectureId}/form/list")
    public ResponseEntity<ApiResponseFormat<LectureApplyFormDto>> getLectureApplyForm(
            @PathVariable Long lectureId
    ) {
        LectureApplyFormDto dto = applicationService.getLectureApplyForm(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(dto));
    }

    @Operation(summary = "과외 신청", description = "특정 과외에 대해 신청을 보냅니다. 신청 시간과 선택 메시지를 포함합니다.")
    @PostMapping("/apply")
    public ResponseEntity<ApiResponseFormat<Void>> applyForLecture(
            @RequestBody LectureApplyRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        applicationService.applyForLecture(dto, userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(
            summary = "매칭된 수강생 목록 조회",
            description = "현재 로그인한 멘토의 매칭 완료된 수강생 목록을 조회합니다."
    )
    @GetMapping("/mentees")
    public ResponseEntity<ApiResponseFormat<List<MenteeResponseDto>>> getMatchedMentees(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<MenteeResponseDto> result = applicationService.getMatchedMenteesByMentor(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(summary = "매칭 취소", description = "승인된 과외 매칭을 취소합니다. 매칭된 멘토만 취소할 수 있으며, 상태를 CANCELLED로 변경합니다.")
    @PostMapping("/cancel/{applicationId}")
    public ResponseEntity<ApiResponseFormat<Void>> cancelApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        applicationService.cancelApplication(applicationId, userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "매칭 ID로 매칭 취소", description = "matchId로 승인된 과외 매칭을 취소합니다.")
    @PostMapping("/cancel/match/{matchId}")
    public ResponseEntity<ApiResponseFormat<Void>> cancelMatchById(
            @PathVariable Long matchId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        applicationService.cancelMatchById(matchId, userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}