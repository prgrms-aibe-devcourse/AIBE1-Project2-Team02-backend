package aibe1.proj2.mentoss.global.entity;

import aibe1.proj2.mentoss.global.entity.enums.EntityStatus;
import aibe1.proj2.mentoss.global.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    private Long userId;
    private String provider;
    private String providerId;
    private String email;
    private String nickname;
    private Long age;
    private String sex;
    private String profileImage;
    private String regionCode;
    private String mbti;
    private String role = UserRole.MENTEE.name();
    private String status = EntityStatus.AVAILABLE.name();
    private Long reportCount = 0L;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
