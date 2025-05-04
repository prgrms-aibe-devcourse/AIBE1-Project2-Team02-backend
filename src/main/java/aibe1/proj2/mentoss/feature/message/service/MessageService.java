package aibe1.proj2.mentoss.feature.message.service;

import aibe1.proj2.mentoss.feature.message.model.dto.MessageResponseDto;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.model.dto.PageResponse;

import java.util.List;

public interface MessageService {

    PageResponse<MessageResponseDto> getSentMessages(Long senderId, int page, int size);;

    PageResponse<MessageResponseDto> getReceivedMessages(Long receiverId, int page, int size);

    MessageResponseDto getMessage(Long messageId, Long viewerId);

    Long sendMessage(MessageSendRequestDto dto, Long senderId);

}
