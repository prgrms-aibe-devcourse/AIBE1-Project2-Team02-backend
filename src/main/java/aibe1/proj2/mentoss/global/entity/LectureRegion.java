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
public class LectureRegion {
    private Long regionId;
    private Long lectureId;
    private String regionCode;
    private LocalDateTime createdAt;
}