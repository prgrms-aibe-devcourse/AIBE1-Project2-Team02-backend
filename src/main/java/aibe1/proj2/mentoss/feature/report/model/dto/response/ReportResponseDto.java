package aibe1.proj2.mentoss.feature.report.model.dto.response;

public record ReportResponseDto(
        Long reportId,
        Long reporterId,
        String targetType,
        Long targetId,
        String reason,
        String reasonType
) {}