package aibe1.proj2.mentoss.feature.application.model.dto;

public record TimeSlot(
        String dayOfWeek,
        String startTime,
        String endTime
) {}