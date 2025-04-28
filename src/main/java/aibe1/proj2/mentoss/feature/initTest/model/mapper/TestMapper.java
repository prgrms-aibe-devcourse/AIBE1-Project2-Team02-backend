package aibe1.proj2.mentoss.feature.initTest.model.mapper;


import aibe1.proj2.mentoss.feature.initTest.model.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {
    @Select("SELECT * FROM t")
    List<TestEntity> findAll();
}
