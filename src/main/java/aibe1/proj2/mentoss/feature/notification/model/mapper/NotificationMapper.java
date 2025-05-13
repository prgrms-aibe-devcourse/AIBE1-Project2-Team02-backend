package aibe1.proj2.mentoss.feature.notification.model.mapper;

import aibe1.proj2.mentoss.feature.notification.model.dto.NotificationResponseDto;
import aibe1.proj2.mentoss.global.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    // 알림 생성
    @Insert("""
        INSERT INTO notification (
          receiver_id, sender_id, type, content, reference_id, target_url, is_read, created_at
        ) VALUES (
          #{receiverId}, #{senderId}, #{type}, #{content}, #{referenceId}, #{targetUrl}, false, CURRENT_TIMESTAMP
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "notificationId")
    void insert(Notification notification);


    // 알림 조회 (최근순)
    @Select("""
    SELECT n.notification_id, n.sender_id, u.nickname, u.profile_image, n.content, n.type, n.target_url, n.is_read, n.created_at
    FROM notification n
    LEFT JOIN app_user u ON n.sender_id = u.user_id
    WHERE n.receiver_id = #{receiverId}
    ORDER BY n.notification_id DESC
    LIMIT #{limit} OFFSET #{offset}
""")
    List<NotificationResponseDto> findByReceiverId(@Param("receiverId") Long receiverId,
                                                   @Param("limit") int limit,
                                                   @Param("offset") int offset);


    // 읽음 처리
    @Update("""
        UPDATE notification
        SET is_read = true
        WHERE receiver_id = #{receiverId} AND is_read = false
    """)
    void markAllAsRead(Long receiverId);


    // 안읽은 알림 개수
    @Select("""
        SELECT COUNT(*)
        FROM notification
        WHERE receiver_id = #{receiverId} AND is_read = false
    """)
    int countUnread(Long receiverId);

}
