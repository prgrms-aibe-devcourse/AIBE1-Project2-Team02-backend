package aibe1.proj2.mentoss.feature.application.model.dto;

public record LectureStatusDto(Long lectureId, Long userId, boolean is_closed, String status) {}