package aibe1.proj2.mentoss.feature.region.service;

import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionResponse;
import aibe1.proj2.mentoss.feature.region.model.mapper.RegionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionMapper regionMapper;

    @Override
    public RegionResponse getAllRegions() {
        List<String> sidos = regionMapper.findAllSidos();
        List<RegionDto> detailedRegions = regionMapper.findAllRegions();
        return new RegionResponse(sidos, null, detailedRegions);
    }

    @Override
    public List<String> getSidos() {
        return regionMapper.findAllSidos();
    }

    @Override
    public List<String> getSigungusBySido(String sido) {
        return regionMapper.findSigungusBySido(sido);
    }

    @Override
    public List<RegionDto> getDongsBySidoAndSigungu(String sido, String sigungu) {
        return regionMapper.findDongsBySidoAndSigungu(sido, sigungu);
    }
}
