package aibe1.proj2.mentoss.feature.region.service;

import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionResponse;

import java.util.List;

public interface RegionService {
    RegionResponse getAllRegions();
    List<String> getSidos();
    List<String> getSigungusBySido(String sido);
    List<RegionDto> getDongsBySidoAndSigungu(String sido, String sigungu);
}