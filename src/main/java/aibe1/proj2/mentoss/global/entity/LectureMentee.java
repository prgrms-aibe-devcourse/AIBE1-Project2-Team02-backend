package aibe1.proj2.mentoss.global.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureMentee {
    private Long matchId;
    private Long lectureId;
    private Long menteeId;
    private Long matchedPrice;
    private String matchedTimeSlots;
    private LocalDateTime joinedAt;
}