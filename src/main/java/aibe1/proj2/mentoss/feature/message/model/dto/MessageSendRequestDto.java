package aibe1.proj2.mentoss.feature.message.model.dto;


import aibe1.proj2.mentoss.global.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageSendRequestDto(

        @NotNull(message = "받는 사용자 ID는 필수입니다.")
        Long receiverId,

        @NotBlank(message = "쪽지 내용을 입력해주세요.")
//        @Size(min = 2, max = 1000, message = "쪽지 내용은 2자 이상 1000자 이하여야 합니다.")
        String content
) {
    public Message toEntity(Long senderId) {
        Message m = new Message();
        m.setSenderId(senderId);
        m.setReceiverId(receiverId);
        m.setContent(content);
        return m;
    }
}

