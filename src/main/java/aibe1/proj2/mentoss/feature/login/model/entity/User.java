package aibe1.proj2.mentoss.feature.login.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String userId;
    private String provider;
    private String providerId;
    private String email;
    private String nickname;
    private Long age;
    private String sex;
    private String profileImage;
    private String regionCode;
    private String mbti;
    private String role;
    private String status;
    private Long reportCount;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
