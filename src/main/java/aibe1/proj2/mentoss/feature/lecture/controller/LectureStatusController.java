package aibe1.proj2.mentoss.feature.lecture.controller;

import aibe1.proj2.mentoss.feature.lecture.service.LectureService;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import aibe1.proj2.mentoss.global.entity.AppUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures")
@Tag(name = "과외 게시글 API", description = "과외게시글 관련 API")
public class LectureStatusController {
    private final LectureService lectureService;

    public LectureStatusController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PatchMapping("/{lectureId}/status")
    @Operation(summary = "강의 마감 상태 변경", description = "강의의 마감 상태를 변경합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "이미 동일한 상태",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<Boolean>> updateLectureStatus(
            @PathVariable Long lectureId,
            @Parameter(description = "강의 마감 상태", required = true,
                    examples = {
                            @ExampleObject(name = "마감", value = "true", summary = "강의를 마감합니다"),
                            @ExampleObject(name = "오픈", value = "false", summary = "강의를 오픈합니다")
                    }
            )
            @RequestParam boolean isClosed,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        AppUser currentUser = lectureService.getUserById(userId);

        // 권한 확인: ADMIN 또는 작성자만 상태 변경 가능
        boolean isAdmin = currentUser.getRole().equals("ADMIN");
        boolean isOwner = lectureService.isLectureOwner(lectureId, userId);

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseFormat.fail("강의 상태 변경 권한이 없습니다."));
        }

        boolean result = lectureService.updateLectureClosed(lectureId, isClosed);

        if (!result) {
            // 이미 동일한 상태인 경우
            String message = isClosed ? "이미 마감된 강의입니다." : "이미 오픈된 강의입니다.";
            return ResponseEntity.badRequest().body(ApiResponseFormat.fail(message));
        }

        return ResponseEntity.ok(ApiResponseFormat.ok(true));
    }
}