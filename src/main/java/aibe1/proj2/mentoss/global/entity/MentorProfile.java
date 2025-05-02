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
public class MentorProfile {
    private Long mentorId;
    private Long userId;
    private String education;
    private String major;
    private Boolean isCertified = false;
    private String content;
    private String appealFileUrl;
    private String tag;
    private LocalDateTime createdAt;
}