package aibe1.proj2.mentoss.global.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String nickname;
    private String content;
    private Boolean isRead = false;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private Boolean senderDeleted;
    private Boolean receiverDeleted;
}