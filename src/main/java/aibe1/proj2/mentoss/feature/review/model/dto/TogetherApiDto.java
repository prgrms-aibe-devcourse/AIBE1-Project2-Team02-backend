package aibe1.proj2.mentoss.feature.review.model.dto;

import java.util.List;

public record TogetherApiDto(String model, List<Message> messages) {
    public record Message(String role, String content) {}
}