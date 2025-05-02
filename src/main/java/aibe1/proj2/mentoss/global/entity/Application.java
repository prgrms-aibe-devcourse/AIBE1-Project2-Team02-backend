package aibe1.proj2.mentoss.global.entity;

import aibe1.proj2.mentoss.global.entity.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    private Long applicationId;
    private Long lectureId;
    private Long menteeId;
    private String requestedTimeSlots;
    private String status = ApplicationStatus.PENDING.name();
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}