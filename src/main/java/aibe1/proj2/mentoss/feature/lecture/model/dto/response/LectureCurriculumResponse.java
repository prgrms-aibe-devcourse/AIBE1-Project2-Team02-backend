package aibe1.proj2.mentoss.feature.lecture.model.dto.response;

/**
 * 강의 커리큘럼 응답 DTO
 */
public record LectureCurriculumResponse(
        Long lectureId,
        String curriculum
) {}
