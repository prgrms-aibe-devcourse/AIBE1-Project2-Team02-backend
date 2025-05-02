package aibe1.proj2.mentoss.feature.lecture.model.dto.response;

import java.util.List;

/**
 * 강의 상세 정보 응답 DTO
 */
public record LectureDetailResponse(
        Long lectureId,
        String lectureTitle,
        String description,
        Long price,
        String timeSlots,
        List<String> regions
) {
    // 생성자 오버로딩 (regions 없는 버전)
    public LectureDetailResponse(Long lectureId, String lectureTitle,
                                 String description, Long price, String timeSlots) {
        this(lectureId, lectureTitle, description, price, timeSlots, null);
    }
}