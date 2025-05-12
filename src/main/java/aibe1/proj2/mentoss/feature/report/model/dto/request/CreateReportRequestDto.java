package aibe1.proj2.mentoss.feature.report.model.dto.request;

public record CreateReportRequestDto(
        Long reporterId,
        String targetType,
        Long targetId,
        String reason,
        String reasonType
) {
}
