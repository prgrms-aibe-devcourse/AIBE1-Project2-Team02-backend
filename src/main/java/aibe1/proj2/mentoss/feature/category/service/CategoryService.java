package aibe1.proj2.mentoss.feature.category.service;

import aibe1.proj2.mentoss.feature.category.model.dto.CategoryResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    /**
     * 모든 카테고리 조회
     */
    List<CategoryResponse> getAllCategories();

    /**
     * 계층 구조로 된 카테고리 트리 조회
     */
    Map<String, Object> getCategoryTree();

    /**
     * 특정 ID의 카테고리 조회
     */
    CategoryResponse getCategoryById(Long categoryId);

    /**
     * 대분류 카테고리 목록 조회
     */
    List<String> getParentCategories();

    /**
     * 특정 대분류에 속한 중분류 목록 조회
     */
    List<String> getMiddleCategories(String parentCategory);

    /**
     * 특정 대분류와 중분류에 속한 소분류 목록 조회
     */
    List<CategoryResponse> getSubcategories(String parentCategory, String middleCategory);
}
