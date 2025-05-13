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
public class Notification {
    private Long notificationId;
    private Long receiverId;
    private Long senderId;
    private String type;
    private String content;
    private Long referenceId;
    private String targetUrl;
    private Boolean isRead;
    private LocalDateTime createdAt;
}