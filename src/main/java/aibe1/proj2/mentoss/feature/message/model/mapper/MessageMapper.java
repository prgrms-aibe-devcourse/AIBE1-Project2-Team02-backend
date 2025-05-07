package aibe1.proj2.mentoss.feature.message.model.mapper;

import aibe1.proj2.mentoss.global.entity.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageMapper {


    @SelectProvider(type = MessageSqlProvider.class, method = "findSentMessagesSql")
    List<Message> findSentMessages(Long userId, int limit, int offset, String filterBy, String keyword);

    @SelectProvider(type = MessageSqlProvider.class, method = "findReceivedMessagesSql")
    List<Message> findReceivedMessages(Long userId, int limit, int offset, String filterBy, String keyword);

    @SelectProvider(type = MessageSqlProvider.class, method = "countSentMessagesSql")
    int countSentMessages(Long userId, String filterBy, String keyword);

    @SelectProvider(type = MessageSqlProvider.class, method = "countReceivedMessagesSql")
    int countReceivedMessages(Long userId, String filterBy, String keyword);


    @Select("""
                SELECT m.*, u.nickname
                FROM message m
                JOIN app_user u ON m.receiver_id = u.user_id
                WHERE m.message_id = #{id}
                  AND m.is_deleted = 0
            """)
    Message findById(Long id);

    @Update("""
                UPDATE message
                SET is_read = 1
                WHERE message_id = #{id}
            """)
    void markAsRead(Long id);

    @Insert("""
                INSERT INTO message (sender_id, receiver_id, content, created_at)
                VALUES (#{senderId}, #{receiverId}, #{content}, #{createdAt})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void insert(Message message);

    @Update("""
                UPDATE message
                SET is_deleted = TRUE, deleted_at = CURRENT_TIMESTAMP
                WHERE sender_id = #{userId} OR receiver_id = #{userId}
            """)
    void softDeleteMessagesByUserId(Long userId);
}
