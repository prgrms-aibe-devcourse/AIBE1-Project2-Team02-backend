package aibe1.proj2.mentoss.feature.message.model.mapper;

import aibe1.proj2.mentoss.global.entity.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageMapper {


    @Select("""
                SELECT * FROM message
                WHERE sender_id = #{userId} AND is_deleted = 0
                ORDER BY created_at DESC
            """)
    List<Message> findSentMessages(Long userId);

    @Select("""
                SELECT * FROM message
                WHERE receiver_id = #{userId} AND is_deleted = 0
                ORDER BY created_at DESC
            """)
    List<Message> findReceivedMessages(Long userId);


    @Select("SELECT * FROM message WHERE message_id = #{id} AND is_deleted = 0")
    Message findById(Long id);

    @Update("""
                UPDATE message
                SET is_read = 1
                WHERE message_id = #{id}
            """)
    void markAsRead(Long id);

    @Insert("""
                INSERT INTO message (sender_id, receiver_id, content)
                VALUES (#{senderId}, #{receiverId}, #{content})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void insert(Message message);

}
