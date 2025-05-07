package aibe1.proj2.mentoss.feature.category.model.dto;

/**
 * 카테고리 응답 DTO
 */
public record CategoryResponse(
        Long categoryId,
        String parentCategory,
        String middleCategory,
        String subcategory
) {}
