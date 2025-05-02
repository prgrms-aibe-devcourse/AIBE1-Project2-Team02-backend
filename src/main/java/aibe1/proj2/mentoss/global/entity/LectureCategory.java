package aibe1.proj2.mentoss.global.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureCategory {
    private Long categoryId;
    private String parentCategory;
    private String middleCategory;
    private String subcategory;
}
