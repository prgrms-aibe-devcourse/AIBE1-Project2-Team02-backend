package aibe1.proj2.mentoss.global.moderation.service;

import aibe1.proj2.mentoss.global.moderation.model.ModerationResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentModerationServiceImpl implements ContentModerationService {

    // Gemini API에 보낼 최대 텍스트 길이 제한 (초과 시 자름)
    private static final int MAX_GEMINI_CONTENT_LENGTH = 8000;

    // Gemini가 허용하는 정확한 응답 목록
    private static final Set<String> VALID_RESPONSES = Set.of(
            "ALLOWED",
            "BLOCKED: Profanity",
            "BLOCKED: Sexual",
            "BLOCKED: Hate",
            "BLOCKED: Violence",
            "BLOCKED: Sensitive"
    );

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.endpoint}")
    private String geminiApiEndpoint;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.prompt.moderation}")
    private String moderationPrompt;

    /**
     * 콘텐츠 필터링을 수행하는 핵심 메서드
     * @param content 유저가 입력한 텍스트
     * @return 유해 여부를 담은 ModerationResult 객체
     */
    @Override
    public ModerationResult moderateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return ModerationResult.allowed();
        }

        // 텍스트 길이 초과 방지 (Gemini 입력 제한)
        String truncated = content.length() > MAX_GEMINI_CONTENT_LENGTH
                ? content.substring(0, MAX_GEMINI_CONTENT_LENGTH)
                : content;

        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Gemini API에 보낼 JSON 구조 구성
            Map<String, Object> textPart = Map.of("text", moderationPrompt + truncated);
            Map<String, Object> contentPart = Map.of("parts", List.of(textPart));

            // 생성 옵션: temperature 낮춤 + 줄바꿈 금지 등
            Map<String, Object> generationConfig = Map.of(
                    "temperature", 0.0,
                    "topP", 1.0,
                    "topK", 1,
                    "maxOutputTokens", 20,
                    "stopSequences", List.of("\n") // 줄바꿈 나오면 자름
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(contentPart));
            requestBody.put("generationConfig", generationConfig);

            // HTTP 요청 객체 생성
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // API 호출
            String url = geminiApiEndpoint + "?key=" + geminiApiKey;
            String response = restTemplate.postForObject(url, request, String.class);

            // 응답 파싱: Gemini의 응답 텍스트 추출
            JsonNode root = objectMapper.readTree(response);
            String rawOutput = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            // 후처리 및 유효한 결과인지 판별
            String cleaned = sanitizeGeminiResponse(rawOutput);
            if (cleaned.startsWith("BLOCKED:")) {
                return ModerationResult.blocked(cleaned.substring("BLOCKED:".length()).trim());
            }
            return ModerationResult.allowed();

        } catch (Exception e) {
            log.error("Gemini moderation error", e);
            // 예외 발생 시 콘텐츠는 허용
            return ModerationResult.allowed();
        }
    }

    /**
     * Gemini 응답을 정리하고 유효한 응답인지 검증
     * @param raw Gemini에서 받은 응답 원문
     * @return 유효한 응답 문자열
     */
    private String sanitizeGeminiResponse(String raw) {
        String normalized = raw.trim()
                .replaceAll("\\r|\\n", "")        // 줄바꿈 제거
                .replaceAll("\\s{2,}", " ")       // 이중 공백 제거
                .toUpperCase();                   // 대소문자 무시 위해 전부 대문자로

        for (String valid : VALID_RESPONSES) {
            if (normalized.contains(valid.toUpperCase())) {
                return valid;
            }
        }

        // 이상한 값이 들어오면 허용
        return "ALLOWED";
    }
}