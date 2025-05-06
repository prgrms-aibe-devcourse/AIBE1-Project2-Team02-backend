package aibe1.proj2.mentoss.feature.account.controller;

import aibe1.proj2.mentoss.feature.account.model.dto.*;
import aibe1.proj2.mentoss.feature.account.service.AccountService;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(name = "Account API", description = "회원 계정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseFormat<ProfileResponseDto>> getProfile(Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        ProfileResponseDto profileDto = accountService.getProfile(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(profileDto));
    }


    @Operation(summary = "프로필 업데이트", description = "로그인한 사용자의 프로필 정보를 업데이트합니다")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponseFormat<Void>> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        accountService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }


    @Operation(summary = "프로필 완성 여부 확인", description = "로그인한 사용자의 프로필이 완성되었는지 확인합니다")
    @GetMapping("/profile/completed")
    public ResponseEntity<ApiResponseFormat<Boolean>> isProfileCompleted(
            Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        boolean isCompleted = accountService.isProfileCompleted(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(isCompleted));
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 계정을 삭제 합니다")
    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponseFormat<Void>> deleteAccount(
            Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        accountService.deleteAccount(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "프로필 이미지 업로드", description = "사용자의 프로필 이미지를 업로드 합니다")
    @PostMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseFormat<String>> uploadProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail("파일이 비어있습니다"));
        }

        long maxSize = 5 * 1024 * 1024;
        if(file.getSize() > maxSize){
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail("파일 크기는 5MB를 초과할 수 없습니다"));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if (!Arrays.asList("jpg", "jpeg", "png", "gif").contains(extension)) {
                return ResponseEntity.badRequest().body(ApiResponseFormat.fail("프로필 이미지는 jpg, jpeg, png, gif 파일만 업로드 가능합니다"));
            }
        } else {
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail("파일 확장자가 없습니다"));
        }


        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        String imageUrl = accountService.updateProfileImage(userId, file);
        return ResponseEntity.ok(ApiResponseFormat.ok(imageUrl));
    }

    @Operation(summary = "멘토 여부 확인", description = "사용자가 멘토인지 확인합니다")
    @GetMapping("/mentor/check")
    public ResponseEntity<ApiResponseFormat<Boolean>> isMentor(
            Authentication authentication
    ) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        boolean isMentor = accountService.isMentor(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(isMentor));
    }

    @Operation(summary = "멘토 프로필 조회", description = "사용자의 멘토 프로필을 조회합니다")
    @GetMapping("/mentor/profile")
    public ResponseEntity<ApiResponseFormat<MentorProfileResponseDto>> getMentorProfile(
            Authentication authentication
    ) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        MentorProfileResponseDto mentorProfile = accountService.getMentorProfile(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(mentorProfile));
    }

    @Operation(summary = "멘토 신청", description = "멘토 프로필을 생성하고 역할을 업데이트합니다")
    @PostMapping(value = "/mentor/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseFormat<Void>> applyMentorProfile(
            Authentication authentication,
            @ModelAttribute MentorProfileRequestDto requestDto
    ) throws IOException {
        MultipartFile file = requestDto.appealFile();

        if (file != null && !file.isEmpty()) {
            long maxSize = 20 * 1024 * 1024;
            if(file.getSize() > maxSize){
                return ResponseEntity.badRequest().body(ApiResponseFormat.fail("어필 파일 크기는 20MB를 초과할 수 없습니다."));
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "ppt", "pptx").contains(extension)) {
                    return ResponseEntity.badRequest().body(ApiResponseFormat.fail("어필 파일은 pdf, jpg, jpeg, png, doc, docx, ppt, pptx 형식만 업로드 가능합니다"));
                }
            } else if (originalFilename != null) {
                return ResponseEntity.badRequest().body(ApiResponseFormat.fail("파일 확장자가 없습니다"));
            }
        }

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        accountService.applyMentorProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "멘토 프로필 업데이트", description = "멘토 프로필 정보를 업데이트합니다")
    @PutMapping(value = "/mentor/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseFormat<Void>> updateMentorProfile(
            Authentication authentication,
            @ModelAttribute MentorProfileRequestDto requestDto
    ) throws IOException {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        accountService.updateMentorProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "멘토 상태 확인", description = "멘토의 인증 상태를 확인합니다")
    @GetMapping("/mentor/status")
    public ResponseEntity<ApiResponseFormat<MentorStatusResponseDto>> getMentorStatus(
            Authentication authentication
    ) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        MentorStatusResponseDto mentorStatus = accountService.getMentorStatus(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(mentorStatus));
    }

    @Operation(summary = "사용자 지역 목록 조회", description = "로그인한 사용자의 저장된 지역 목록을 조회합니다")
    @GetMapping("/regions")
    public ResponseEntity<ApiResponseFormat<List<RegionDto>>> getUserRegions(
            Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        List<RegionDto> regions = accountService.getUserRegions(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(regions));
    }

    @Operation(summary = "사용자 지역 업데이트", description = "로그인한 사용자의 지역 정보를 업데이트합니다")
    @PutMapping("/regions")
    public ResponseEntity<ApiResponseFormat<Void>> updateUserRegions(
            Authentication authentication,
            @RequestBody UserRegionsUpdateRequestDto requestDto) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        accountService.updateUserRegion(userId, requestDto);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "로그아웃", description = "사용자를 로그아웃합니다")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseFormat<Void>> logout() {
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }
}
