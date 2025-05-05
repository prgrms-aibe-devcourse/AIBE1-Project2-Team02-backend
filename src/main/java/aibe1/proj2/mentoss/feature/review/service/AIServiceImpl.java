package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiResponseDto;
import aibe1.proj2.mentoss.feature.review.model.mapper.AIMapper;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.review.TogetherApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    private final AIMapper aiMapper;
    private final ReviewMapper reviewMapper;


    @Value("${together.api-key}")
    private String apiKey;
    @Value("${together.model}")
    private String model;
    @Value("${together.prompt}")
    private String prePrompt;

    @Override
    public TagResponseDto answer(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(new TogetherApiDto(model, List.of(new TogetherApiDto.Message("user", prompt))));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.together.xyz/v1/chat/completions"))
                .header("Authorization", "Bearer %s".formatted(apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return new TagResponseDto(mapper.readValue(response.body(), TogetherApiResponseDto.class).choices().get(0).message().content());
        }
        throw new TogetherApiException("TogetherAI API 응답이 올바르지 않습니다. Api Status : " + response.statusCode());
    }

    @Override
    public String createPrompt(Long mentorId) {
        if (!reviewMapper.existsMentor(mentorId)) {
            throw new ResourceNotFoundException("Mentor", mentorId);
        }
        List<ReviewResponseDto> reviews = aiMapper.selectReviewsByMentorId(mentorId);
        String allContents = reviews.stream()
                .map(ReviewResponseDto::content)
                .collect(Collectors.joining("\n"));

        return prePrompt + allContents;
    }

    @Override
    public void updateMentorTag(Long mentorId, String tag) {
        aiMapper.updateMentorTag(mentorId, tag);
    }
}
