package aibe1.proj2.mentoss.feature.report.model.dto;

public record ReportProcessRequestDto(
        Long reportId,
        Long adminId,
        String actionType,
        String reason,
        Long suspendPeriod
) {
}
