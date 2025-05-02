package aibe1.proj2.mentoss.global.entity;

import aibe1.proj2.mentoss.global.entity.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    private Long lectureId;
    private Long mentorId;
    private Long categoryId;
    private String curriculum;
    private Long price;
    private String availableTimeSlots;
    private String lectureTitle;
    private String description;
    private Boolean isClosed = false;
    private String status = EntityStatus.AVAILABLE.name();
    private Long reportCount;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}