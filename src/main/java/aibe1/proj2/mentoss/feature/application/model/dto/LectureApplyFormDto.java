package aibe1.proj2.mentoss.feature.application.model.dto;

import java.util.List;

public record LectureApplyFormDto(
        Long lectureId,
        String lectureTitle,
        List<TimeSlot> availableTimeSlots,
        String profileImage,
        String nickname,
        String education,
        String major,
        boolean isCertified,
        double averageRating
) {
}
