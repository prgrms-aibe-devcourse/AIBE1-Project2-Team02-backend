package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiResponseDto;
import aibe1.proj2.mentoss.feature.review.model.mapper.AIMapper;
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


    @Value("${together.api-key}")
    private String apiKey;
    @Value("${together.model}")
    private String model;

    @Override
    public String answer(String prompt) throws Exception {
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
            return mapper.readValue(response.body(), TogetherApiResponseDto.class).choices().get(0).message().content();
        }
        throw new Exception("Inavlid response");
    }

    @Override
    public String createPrompt(Long mentorId) {
        List<ReviewResponseDto> reviews = aiMapper.selectReviewsByMentorId(mentorId);
        String allContents = reviews.stream()
                .map(ReviewResponseDto::content)
                .collect(Collectors.joining("\n"));

        return "아래 후기들은 과외 강사에게 달린 후기 10개 입니다. 이 내용을 바탕으로 이 강사의 특징을 파악해 짧고 간결하게 한 문장으로 요약해주세요. :\n" + allContents;
    }
}
