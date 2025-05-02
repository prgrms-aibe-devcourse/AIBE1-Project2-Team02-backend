package aibe1.proj2.mentoss.feature.lecture.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    private Long lectureId;
    private String lectureTitle;
    private String description;
    private Long categoryId;
    private String curriculum;
    private Long price;
    private Long mentorId;
    private String availableTimeSlots;
    private boolean isClosed;
    private String status;
    private Long reportCount;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
