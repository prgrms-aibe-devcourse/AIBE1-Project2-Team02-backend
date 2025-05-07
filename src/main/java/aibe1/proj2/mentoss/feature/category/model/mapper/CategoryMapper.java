package aibe1.proj2.mentoss.feature.category.model.mapper;

import aibe1.proj2.mentoss.feature.category.model.dto.CategoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {

    /**
     * 모든 카테고리 조회
     */
    @Select("SELECT category_id AS categoryId, parent_category AS parentCategory, " +
            "middle_category AS middleCategory, subcategory " +
            "FROM lecture_category")
    List<CategoryResponse> getAllCategories();

    /**
     * 특정 ID의 카테고리 조회
     */
    @Select("SELECT category_id AS categoryId, parent_category AS parentCategory, " +
            "middle_category AS middleCategory, subcategory " +
            "FROM lecture_category WHERE category_id = #{categoryId}")
    CategoryResponse getCategoryById(Long categoryId);

    /**
     * 대분류 카테고리 목록 조회
     */
    @Select("SELECT DISTINCT parent_category AS parentCategory FROM lecture_category")
    List<String> getParentCategories();

    /**
     * 특정 대분류에 속한 중분류 목록 조회
     */
    @Select("SELECT DISTINCT middle_category AS middleCategory FROM lecture_category WHERE parent_category = #{parentCategory}")
    List<String> getMiddleCategories(String parentCategory);

    /**
     * 특정 대분류와 중분류에 속한 소분류 목록 조회
     */
    @Select("SELECT category_id AS categoryId, parent_category AS parentCategory, " +
            "middle_category AS middleCategory, subcategory " +
            "FROM lecture_category " +
            "WHERE parent_category = #{parentCategory} AND middle_category = #{middleCategory}")
    List<CategoryResponse> getSubcategories(String parentCategory, String middleCategory);
}
