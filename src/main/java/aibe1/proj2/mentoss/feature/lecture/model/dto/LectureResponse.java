package aibe1.proj2.mentoss.feature.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 강의 기본 정보 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponse {
    private Long lectureId;
    private String lectureTitle;
    private String mentorNickname;
    private LocalDateTime createdAt;
    private String parentCategory;
    private String middleCategory;
    private String subcategory;
    private boolean isClosed;
    private String status;
}