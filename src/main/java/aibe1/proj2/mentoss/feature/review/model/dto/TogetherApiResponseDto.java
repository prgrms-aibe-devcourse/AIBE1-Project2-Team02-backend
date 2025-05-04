package aibe1.proj2.mentoss.feature.review.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TogetherApiResponseDto(List<Choice> choices) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(Message message) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(String content) {}
}
