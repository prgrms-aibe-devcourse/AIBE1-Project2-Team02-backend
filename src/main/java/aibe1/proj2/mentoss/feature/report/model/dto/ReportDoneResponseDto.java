package aibe1.proj2.mentoss.feature.report.model.dto;

import java.time.LocalDateTime;

public record ReportDoneResponseDto(
        Long reportId,
        Long reporterId,
        String targetType,
        Long targetId,
        String reason,
        String reasonType,
        LocalDateTime processedAt,
        Long processAdminId
) {
}
