package aibe1.proj2.mentoss.feature.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 강의 상세 정보 응답 DTO (일반 클래스로 변경)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDetailResponse {
    private Long lectureId;
    private String lectureTitle;
    private String description;
    private Long price;
    private String timeSlots;
    private List<String> regions;

    public LectureDetailResponse(Long lectureId, String lectureTitle, String description, Long price, String timeSlots) {
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.description = description;
        this.price = price;
        this.timeSlots = timeSlots;
    }
}