package aibe1.proj2.mentoss.feature.region.model.dto;

public record RegionDto(
        String regionCode,
        String sido,
        String sigungu,
        String dong,
        String displayName  // 화면에 보여줄 텍스트 (e.g., "서울특별시 강남구 일원동")
) {}