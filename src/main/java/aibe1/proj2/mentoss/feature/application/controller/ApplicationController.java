package aibe1.proj2.mentoss.feature.application.controller;

import aibe1.proj2.mentoss.feature.application.model.dto.*;
import aibe1.proj2.mentoss.feature.application.service.ApplicationService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final Long menteeId = 10L;
    private final Long mentorId = 1L;

    @Operation(
            summary = "내가 신청한 과외 목록 조회",
            description = "현재 로그인한 사용자가 신청한 과외 목록을 조회합니다."
    )
    @GetMapping("/apply/list")
    public ResponseEntity<ApiResponseFormat<List<AppliedLectureResponseDto>>> getMyAppliedLectures() {
        List<AppliedLectureResponseDto> result = applicationService.getMyAppliedLectures(menteeId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "내가 등록한 과외 목록 조회",
            description = "현재 로그인한 사용자가 등록한 과외 목록을 조회합니다."
    )
    @GetMapping("/apply/lectures/list")
    public ResponseEntity<ApiResponseFormat<List<LectureResponseDto>>> getLecturesByMentor() {
        List<LectureResponseDto> result = applicationService.getLecturesByMentor(mentorId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "신청한 멘티 리스트 조회",
            description = "특정 과외에 대해 신청한 멘티들의 목록을 조회합니다."
    )
    @GetMapping("/{lectureId}/applicants")
    public ResponseEntity<ApiResponseFormat<List<LectureApplicantDto>>> getLectureApplicants(
            @PathVariable Long lectureId
    ) {
        List<LectureApplicantDto> applicants = applicationService.getApplicantsByLectureId(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(applicants));
    }

    @Operation(summary = "과외 신청 수락", description = "applicationId를 받아 해당 신청을 수락 처리합니다.")
    @PostMapping("/approve")
    public ResponseEntity<ApiResponseFormat<Void>> approveApplication(
            @RequestBody ApproveApplicationRequestDto dto
    ) {
        applicationService.approveApplication(dto.applicationId(), mentorId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "과외 신청 반려", description = "applicationId와 반려 사유를 받아 신청을 반려 처리합니다.")
    @PostMapping("/reject")
    public ResponseEntity<ApiResponseFormat<Void>> rejectApplication(
            @RequestBody RejectApplicationRequestDto dto
            ) {
        applicationService.rejectApplication(dto.applicationId(), dto.reason(), mentorId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
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
            @RequestBody LectureApplyRequestDto dto
    ) {
        applicationService.applyForLecture(dto, menteeId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}