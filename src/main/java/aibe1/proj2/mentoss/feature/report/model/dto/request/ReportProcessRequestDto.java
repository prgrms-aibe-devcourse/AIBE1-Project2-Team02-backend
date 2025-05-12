package aibe1.proj2.mentoss.feature.report.model.dto.request;

public record ReportProcessRequestDto(
        Long reportId,
        String actionType,
        String reason,
        Long suspendPeriod
) {
}
