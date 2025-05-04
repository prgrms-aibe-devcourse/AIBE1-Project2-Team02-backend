package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateTagRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;
import aibe1.proj2.mentoss.feature.review.service.AIService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping("/tag")
    public ResponseEntity<ApiResponseFormat<TagResponseDto>> generateMentorTag(
            @RequestBody CreateTagRequestDto dto) throws Exception {

        String prompt = aiService.createPrompt(dto.mentorId());

        TagResponseDto response = aiService.answer(prompt);

        aiService.updateMentorTag(dto.mentorId(), response.tag());

        return ResponseEntity.ok(ApiResponseFormat.ok(response));
    }
}
