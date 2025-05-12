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
public class Report {
    private Long reportId;
    private Long reporterId;
    private String targetType;
    private Long targetId;
    private String reason;
    private String reasonType;
    private Boolean isProcessed = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}