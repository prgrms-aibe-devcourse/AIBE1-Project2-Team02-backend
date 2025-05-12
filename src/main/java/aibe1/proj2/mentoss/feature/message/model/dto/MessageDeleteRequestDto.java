package aibe1.proj2.mentoss.feature.message.model.dto;

import java.util.List;

public record MessageDeleteRequestDto(List<Long> messageIds) {
}
