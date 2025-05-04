package aibe1.proj2.mentoss.feature.application.controller;

import aibe1.proj2.mentoss.feature.application.model.dto.AppliedLectureResponseDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureApplicantDto;
import aibe1.proj2.mentoss.feature.application.model.dto.LectureResponseDto;
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
    @GetMapping("/my-applied")
    public ResponseEntity<ApiResponseFormat<List<AppliedLectureResponseDto>>> getMyAppliedLectures() {
        List<AppliedLectureResponseDto> result = applicationService.getMyAppliedLectures(menteeId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "내가 등록한 과외 목록 조회",
            description = "현재 로그인한 사용자가 등록한 과외 목록을 조회합니다."
    )
    @GetMapping("/my-lectures")
    public ResponseEntity<ApiResponseFormat<List<LectureResponseDto>>> getLecturesByMentor() {
        List<LectureResponseDto> result = applicationService.getLecturesByMentor(mentorId);
        return ResponseEntity.ok(ApiResponseFormat.ok(result));
    }

    @Operation(
            summary = "신청한 멘티 리스트 조회",
            description = "특정 강의에 대해 신청한 멘티들의 목록을 조회합니다."
    )
    @GetMapping("/{lectureId}/applicants")
    public ResponseEntity<ApiResponseFormat<List<LectureApplicantDto>>> getLectureApplicants(
            @PathVariable Long lectureId
    ) {
        List<LectureApplicantDto> applicants = applicationService.getApplicantsByLectureId(lectureId);
        return ResponseEntity.ok(ApiResponseFormat.ok(applicants));
    }
}