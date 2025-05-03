package aibe1.proj2.mentoss.feature.message.service;

import aibe1.proj2.mentoss.feature.message.model.dto.MessageResponseDto;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;

import java.util.List;

public interface MessageService {

    List<MessageResponseDto> getSentMessages(Long senderId);

    List<MessageResponseDto> getReceivedMessages(Long receiverId);

    MessageResponseDto getMessage(Long messageId, Long viewerId);

    Long sendMessage(MessageSendRequestDto dto, Long senderId);

}
