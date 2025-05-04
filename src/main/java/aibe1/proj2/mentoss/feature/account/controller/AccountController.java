package aibe1.proj2.mentoss.feature.account.controller;

import aibe1.proj2.mentoss.feature.account.model.dto.ProfileResponseDto;
import aibe1.proj2.mentoss.feature.account.service.AccountService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account API", description = "회원 계정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseFormat<ProfileResponseDto>> getProfile(Authentication authentication) {
        // 개발 중에는 임시로 고정 ID 사용
        Long userId = 1L; // 나중에 실제 인증 구현으로 대체
        ProfileResponseDto profileDto = accountService.getProfile(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(profileDto));

    }
}
