package aibe1.proj2.mentoss.feature.message.controller;

import aibe1.proj2.mentoss.feature.message.model.dto.MessageResponseDto;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.service.MessageService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Message API", description = "쪽지 관련 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    // JWT 토큰을 기반으로 현재 user id를 받을 예정
    private final Long userId = 1L;

    @Operation(summary = "보낸 편지함 조회", description = "현재 로그인한 사용자가 보낸 메시지를 조회합니다.")
    @GetMapping("/sent")
    public ResponseEntity<ApiResponseFormat<List<MessageResponseDto>>> getSentMessages(Authentication authentication) {
//        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        List<MessageResponseDto> messages = messageService.getSentMessages(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(messages));
    }

    @Operation(summary = "받은 편지함 조회", description = "현재 로그인한 사용자가 받은 메시지를 조회합니다.")
    @GetMapping("/received")
    public ResponseEntity<ApiResponseFormat<List<MessageResponseDto>>> getReceivedMessages(Authentication authentication) {
        List<MessageResponseDto> messages = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(messages));
    }

    @Operation(summary = "쪽지 조회", description = "쪽지 ID를 통해 쪽지를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseFormat<MessageResponseDto>> getMessage(@PathVariable Long id) {
        MessageResponseDto message = messageService.getMessage(id, userId);
        return ResponseEntity.ok(ApiResponseFormat.ok(message));
    }

    @Operation(summary = "쪽지 보내기", description = "받은 사람 ID, 내용을 입력해 쪽지를 보냅니다.")
    @PostMapping
    public ResponseEntity<ApiResponseFormat<Long>> sendMessage(@RequestBody MessageSendRequestDto dto) {
        Long messageId = messageService.sendMessage(dto, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFormat.ok(messageId));
    }
}
