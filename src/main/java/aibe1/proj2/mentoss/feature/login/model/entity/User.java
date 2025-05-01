package aibe1.proj2.mentoss.feature.login.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
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
    private String status = UserStatus.AVAILABLE.name();
    private Long reportCount = 0L;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
