package aibe1.proj2.mentoss.feature.application.model.dto;

public record RejectApplicationRequestDto(
        Long applicationId,
        String reason
) {
}
