package aibe1.proj2.mentoss.feature.notification.controller;

import aibe1.proj2.mentoss.feature.notification.model.dto.NotificationResponseDto;
import aibe1.proj2.mentoss.feature.notification.service.NotificationService;
import aibe1.proj2.mentoss.global.auth.CustomUserDetails;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Notification API", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "최근 알림 조회", description = "로그인한 사용자의 최근 알림을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponseFormat<List<NotificationResponseDto>>> getRecentNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<NotificationResponseDto> notifications = notificationService.getRecentNotifications(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(notifications));
    }

    @Operation(summary = "모든 알림 읽음 처리", description = "모든 알림을 읽음 상태로 변경합니다.")
    @PostMapping("/read-all")
    public ResponseEntity<ApiResponseFormat<Void>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(null));
    }

    @Operation(summary = "읽지 않은 알림 개수", description = "읽지 않은 알림 개수를 반환합니다.")
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponseFormat<Integer>> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        int count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(count));
    }
}