package aibe1.proj2.mentoss.feature.category.controller;

import aibe1.proj2.mentoss.feature.category.model.dto.CategoryResponse;
import aibe1.proj2.mentoss.feature.category.service.CategoryService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "카테고리 API", description = "강의 카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "강의 카테고리 전체 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponseFormat.ok(categories));
    }

    @GetMapping("/tree")
    @Operation(summary = "카테고리 트리 조회", description = "카테고리를 계층 구조(대분류->중분류->소분류)로 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<Map<String, Object>>> getCategoryTree() {
        Map<String, Object> categoryTree = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponseFormat.ok(categoryTree));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "특정 카테고리 조회", description = "ID로 특정 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "카테고리 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<CategoryResponse>> getCategoryById(
            @PathVariable Long categoryId) {
        CategoryResponse category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponseFormat.ok(category));
    }

    @GetMapping("/parents")
    @Operation(summary = "대분류 카테고리 목록 조회", description = "모든 대분류 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<List<String>>> getParentCategories() {
        List<String> parentCategories = categoryService.getParentCategories();
        return ResponseEntity.ok(ApiResponseFormat.ok(parentCategories));
    }

    @GetMapping("/middles")
    @Operation(summary = "중분류 카테고리 목록 조회", description = "특정 대분류에 속한 중분류 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<List<String>>> getMiddleCategories(
            @Parameter(description = "대분류 카테고리", required = true)
            @RequestParam String parentCategory) {
        List<String> middleCategories = categoryService.getMiddleCategories(parentCategory);
        return ResponseEntity.ok(ApiResponseFormat.ok(middleCategories));
    }

    @GetMapping("/subs")
    @Operation(summary = "소분류 카테고리 목록 조회", description = "특정 대분류와 중분류에 속한 소분류 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseFormat.class))
            )
    })
    public ResponseEntity<ApiResponseFormat<List<CategoryResponse>>> getSubcategories(
            @Parameter(description = "대분류 카테고리", required = true)
            @RequestParam String parentCategory,
            @Parameter(description = "중분류 카테고리", required = true)
            @RequestParam String middleCategory) {
        List<CategoryResponse> subcategories = categoryService.getSubcategories(parentCategory, middleCategory);
        return ResponseEntity.ok(ApiResponseFormat.ok(subcategories));
    }
}
