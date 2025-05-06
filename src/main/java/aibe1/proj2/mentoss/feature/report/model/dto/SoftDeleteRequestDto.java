package aibe1.proj2.mentoss.feature.report.model.dto;

import java.time.LocalDateTime;

public record SoftDeleteRequestDto(String targetType, Long targetId) {
}
