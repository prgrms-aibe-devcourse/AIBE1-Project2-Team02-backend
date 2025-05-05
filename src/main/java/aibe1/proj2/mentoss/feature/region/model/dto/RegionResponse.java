package aibe1.proj2.mentoss.feature.region.model.dto;

import java.util.List;

public record RegionResponse(
        List<String> sidos,
        List<String> sigungus,
        List<RegionDto> detailedRegions
) {}
