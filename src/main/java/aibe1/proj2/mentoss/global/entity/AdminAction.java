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
public class AdminAction {
    private Long actionId;
    private Long adminId;
    private String targetType;
    private Long targetId;
    private String actionType;
    private String reason;
    private LocalDateTime actionTime;
    private Long suspensionPeriodDays;
}