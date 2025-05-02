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
public class Review {
    private Long reviewId;
    private Long lectureId;
    private Long mentorId;
    private Long writerId;
    private String content;
    private Long rating;
    private String status = EntityStatus.AVAILABLE.name();
    private Long reportCount = 0L;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
}