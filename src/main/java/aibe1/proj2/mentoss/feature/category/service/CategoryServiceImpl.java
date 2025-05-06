package aibe1.proj2.mentoss.feature.category.service;

import aibe1.proj2.mentoss.feature.category.model.dto.CategoryResponse;
import aibe1.proj2.mentoss.feature.category.model.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    @Override
    public Map<String, Object> getCategoryTree() {
        List<CategoryResponse> allCategories = categoryMapper.getAllCategories();
        Map<String, Object> result = new HashMap<>();

        // 대분류 -> 중분류 -> 소분류 형태의 트리 구조 생성
        for (CategoryResponse category : allCategories) {
            if (!result.containsKey(category.parentCategory())) {
                result.put(category.parentCategory(), new HashMap<String, Object>());
            }

            Map<String, Object> middleMap = (Map<String, Object>) result.get(category.parentCategory());
            if (!middleMap.containsKey("middle")) {
                middleMap.put("middle", new HashMap<String, Object>());
            }

            Map<String, Object> middleCategoriesMap = (Map<String, Object>) middleMap.get("middle");
            if (!middleCategoriesMap.containsKey(category.middleCategory())) {
                middleCategoriesMap.put(category.middleCategory(), new HashMap<String, Object>());
            }

            Map<String, Object> subcategoriesMap = (Map<String, Object>) middleCategoriesMap.get(category.middleCategory());
            subcategoriesMap.put(category.subcategory(), category.categoryId());
        }

        return result;
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        return categoryMapper.getCategoryById(categoryId);
    }

    @Override
    public List<String> getParentCategories() {
        return categoryMapper.getParentCategories();
    }

    @Override
    public List<String> getMiddleCategories(String parentCategory) {
        return categoryMapper.getMiddleCategories(parentCategory);
    }

    @Override
    public List<CategoryResponse> getSubcategories(String parentCategory, String middleCategory) {
        return categoryMapper.getSubcategories(parentCategory, middleCategory);
    }
}
