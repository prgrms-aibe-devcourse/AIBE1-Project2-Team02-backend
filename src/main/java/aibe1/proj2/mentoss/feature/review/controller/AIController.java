package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.service.AIService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping("/mentor/{mentorId}/tag")
    public ResponseEntity<ApiResponseFormat<String>> generateMentorTag(
            @PathVariable Long mentorId) throws Exception {

        String prompt = aiService.createPrompt(mentorId);

        String tag = aiService.answer(prompt);

        // 3) 멘토 프로필의 tag 컬럼에 저장 (Service/Mapper 구현부에서 처리)
        // mentorProfileService.updateTag(mentorId, tag);

        // 4) 생성된 태그를 응답
        return ResponseEntity.ok(ApiResponseFormat.ok(tag));
    }
}
