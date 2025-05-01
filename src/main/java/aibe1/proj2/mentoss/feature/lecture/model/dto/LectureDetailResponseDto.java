package aibe1.proj2.mentoss.feature.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 강의 상세 정보 응답 DTO (클라이언트에게 반환)
 * JSON으로 파싱된 timeSlots를 포함
 */
@Data
@AllArgsConstructor
public class LectureDetailResponseDto {
    private Long lectureId;
    private String lectureTitle;
    private String description;
    private Long price;
    private List<String> regions;
    private List<TimeSlotResponse> timeSlots;
}