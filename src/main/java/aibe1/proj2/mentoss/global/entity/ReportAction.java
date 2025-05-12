package aibe1.proj2.mentoss.global.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportAction {
    private Long reportActionId;
    private Long reportId;
    private Long actionId;
    private LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
}