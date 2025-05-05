package aibe1.proj2.mentoss.feature.region.controller;

import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import aibe1.proj2.mentoss.feature.region.model.dto.RegionResponse;
import aibe1.proj2.mentoss.feature.region.service.RegionService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@Tag(name = "지역 API", description = "지역 정보 조회 API")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/sidos")
    @Operation(summary = "시도 목록 조회", description = "모든 시도 목록을 조회합니다.")
    public ResponseEntity<ApiResponseFormat<List<String>>> getSidos() {
        List<String> sidos = regionService.getSidos();
        return ResponseEntity.ok(ApiResponseFormat.ok(sidos));
    }

    @GetMapping("/sigungus")
    @Operation(summary = "시군구 목록 조회", description = "특정 시도의 시군구 목록을 조회합니다.")
    public ResponseEntity<ApiResponseFormat<List<String>>> getSigungus(
            @RequestParam String sido) {
        List<String> sigungus = regionService.getSigungusBySido(sido);
        return ResponseEntity.ok(ApiResponseFormat.ok(sigungus));
    }

    @GetMapping("/dongs")
    @Operation(summary = "읍면동 목록 조회", description = "특정 시군구의 읍면동 목록을 조회합니다.")
    public ResponseEntity<ApiResponseFormat<List<RegionDto>>> getDongs(
            @RequestParam String sido,
            @RequestParam String sigungu) {
        List<RegionDto> dongs = regionService.getDongsBySidoAndSigungu(sido, sigungu);
        return ResponseEntity.ok(ApiResponseFormat.ok(dongs));
    }

    @GetMapping
    @Operation(summary = "전체 지역 정보 조회", description = "전체 지역 정보를 조회합니다.")
    public ResponseEntity<ApiResponseFormat<RegionResponse>> getAllRegions() {
        RegionResponse regions = regionService.getAllRegions();
        return ResponseEntity.ok(ApiResponseFormat.ok(regions));
    }
}
