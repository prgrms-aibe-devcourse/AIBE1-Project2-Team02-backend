package aibe1.proj2.mentoss.feature.notification.service;

import aibe1.proj2.mentoss.feature.notification.model.dto.NotificationResponseDto;
import aibe1.proj2.mentoss.feature.notification.model.mapper.NotificationMapper;
import aibe1.proj2.mentoss.global.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponseDto> getRecentNotifications(Long receiverId) {
        return notificationMapper.findByReceiverId(receiverId, 30, 0);
    }

    @Override
    public void createNotification(Long receiverId, Long senderId, String type, String content, Long referenceId, String targetUrl) {
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(type)
                .content(content)
                .referenceId(referenceId)
                .targetUrl(targetUrl)
                .isRead(false)
                .build();

        notificationMapper.insert(notification);
    }

    @Override
    public void markAllAsRead(Long receiverId) {
        notificationMapper.markAllAsRead(receiverId);
    }

    @Override
    public int getUnreadCount(Long receiverId) {
        return notificationMapper.countUnread(receiverId);
    }
}