package aibe1.proj2.mentoss.global.moderation.service;

import aibe1.proj2.mentoss.global.moderation.model.ModerationResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Gemini API를 이용하여 콘텐츠 유해성 여부를 판별하는 서비스 구현체
 */

// TODO: 토큰 초과 시 글 등록은 허용하되, 관리자에게 알림?
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentModerationServiceImpl implements ContentModerationService {

    private static final int MAX_GEMINI_CONTENT_LENGTH = 8000;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.endpoint}")
    private String geminiApiEndpoint;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.prompt.moderation}")
    private String moderationPrompt;

    /**
     * 주어진 텍스트에 대해 유해한 콘텐츠가 포함되어 있는지를 판별합니다.
     *
     * @param content 검사할 텍스트
     * @return 유해 여부 및 차단 사유를 포함한 ModerationResult 객체
     */
    @Override
    public ModerationResult moderateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return ModerationResult.allowed();
        }

        // 길이 제한 적용
        String truncated = content.length() > MAX_GEMINI_CONTENT_LENGTH
                ? content.substring(0, MAX_GEMINI_CONTENT_LENGTH)
                : content;

        try {
            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Gemini API 요청 본문 구성
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> contents = new HashMap<>();
            Map<String, Object> parts = new HashMap<>();
            
            parts.put("text", moderationPrompt + truncated);
            contents.put("parts", new Object[]{parts});
            requestBody.put("contents", new Object[]{contents});
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Gemini API 호출
            String url = geminiApiEndpoint + "?key=" + geminiApiKey;
            String response = restTemplate.postForObject(url, request, String.class);

            // 응답 결과 파싱
            JsonNode responseJson = objectMapper.readTree(response);
            String generatedText = responseJson
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            // 유해 여부 판별
            if (generatedText.startsWith("BLOCKED:")) {
                String reason = generatedText.substring("BLOCKED:".length()).trim();
                return ModerationResult.blocked(reason);
            } else {
                return ModerationResult.allowed();
            }
        } catch (Exception e) {
            log.error("Error during content moderation", e);
            // 일단 지금은 오류 발생 시, 콘텐츠 차단을 방지하고 통과 처리
            return ModerationResult.allowed();
        }
    }
}