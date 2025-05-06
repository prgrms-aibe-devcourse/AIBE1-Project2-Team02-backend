package aibe1.proj2.mentoss.feature.account.model.dto;

import java.util.List;

public record UserRegionsUpdateRequestDto(
        List<String> regionCodes
) {
}
