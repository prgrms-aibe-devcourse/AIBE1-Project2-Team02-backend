package aibe1.proj2.mentoss.feature.review.service;


import aibe1.proj2.mentoss.feature.review.model.dto.ReviewResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TogetherApiResponseDto;
import aibe1.proj2.mentoss.feature.review.model.mapper.AiMapper;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.review.TogetherApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {
    private final AiMapper aiMapper;
    private final ReviewMapper reviewMapper;


    @Value("${together.api-key1}")
    private String apiKey1;
    @Value("${together.api-key2}")
    private String apiKey2;
    @Value("${together.api-key3}")
    private String apiKey3;
    @Value("${together.model}")
    private String model;
    @Value("${together.prompt}")
    private String prePrompt;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private List<String> apiKeys;


    private final AtomicInteger keyIndex = new AtomicInteger(0);


    @PostConstruct
    public void init() {
        this.apiKeys = List.of(apiKey1, apiKey2, apiKey3);
    }

    private String nextApiKey() {
        int idx = keyIndex.getAndUpdate(i -> (i + 1) % apiKeys.size());
        return apiKeys.get(idx);
    }


    @Override
    public TagResponseDto answer(String prompt){
        int attempts = apiKeys.size();
        for (int i = 0; i < attempts; i++) {
            String currentKey = nextApiKey();
            try {
                return callTogetherApi(prompt, currentKey);
            } catch (TogetherApiException tae) {
                if (tae.getStatus() == 429 && i < attempts - 1) {
                    log.warn("429 발생, 다음 API 키로 재시도 ({} / {})", i+1, attempts);
                    continue;
                }
                throw tae;
            }
        }
        throw new TogetherApiException("모든 API 키로 재시도 실패", -1);
    }

    private TagResponseDto callTogetherApi(String prompt, String apiKey){
        try{
            String body = mapper.writeValueAsString(new TogetherApiDto(model, List.of(new TogetherApiDto.Message("user", prompt))));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.together.xyz/v1/chat/completions"))
                    .header("Authorization", "Bearer %s".formatted(apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String tag = mapper.readValue(response.body(), TogetherApiResponseDto.class).choices().get(0).message().content();
                if(tag.equals("-")){
                    return new TagResponseDto("");
                }
                else {
                    return new TagResponseDto(tag);
                }
            }
            throw new TogetherApiException("TogetherAI API 응답이 올바르지 않습니다. Api Status : " , response.statusCode());
        }catch (IOException | InterruptedException ex) {
            throw new TogetherApiException("API 호출 중 예외: " + ex.getMessage(), -1);
        }
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
