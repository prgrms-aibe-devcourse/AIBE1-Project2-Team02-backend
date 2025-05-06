package aibe1.proj2.mentoss.feature.region.model.mapper;

import aibe1.proj2.mentoss.feature.region.model.dto.RegionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RegionMapper {

    @Select("SELECT DISTINCT sido FROM region ORDER BY sido")
    List<String> findAllSidos();

    @Select("SELECT DISTINCT sigungu FROM region WHERE sido = #{sido} ORDER BY sigungu")
    List<String> findSigungusBySido(String sido);

    @Select("SELECT region_code AS regionCode, sido, sigungu, COALESCE(dong, '') AS dong, " +
            "CONCAT(sido, ' ', sigungu, ' ', COALESCE(dong, '')) AS displayName " +
            "FROM region WHERE sido = #{sido} AND sigungu = #{sigungu} ORDER BY dong")
    List<RegionDto> findDongsBySidoAndSigungu(String sido, String sigungu);

    @Select("SELECT region_code AS regionCode, sido, sigungu, COALESCE(dong, '') AS dong, " +
            "CONCAT(sido, ' ', sigungu, ' ', COALESCE(dong, '')) AS displayName " +
            "FROM region")
    List<RegionDto> findAllRegions();
}