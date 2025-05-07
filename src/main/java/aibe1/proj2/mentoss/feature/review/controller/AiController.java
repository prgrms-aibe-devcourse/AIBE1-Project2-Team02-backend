package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.CreateTagRequestDto;
import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;
import aibe1.proj2.mentoss.feature.review.service.AIService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI API", description = "생성형 AI 활용 멘토 태그 생성 API")
@RequiredArgsConstructor
public class AiController {
    private final AIService aiService;



    @Operation(summary = "멘토 태그 생성", description = "지정한 멘토의 리뷰를 바탕으로 태그를 생성하고 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                              "success": true,
                                              "message": "요청이 성공적으로 처리되었습니다.",
                                              "data": {
                                                "tag": "이 강사는 실습 위주로 진행하며 명확한 설명과 풍부한 예제를 제공하여 학생들의 실력 향상에 큰 도움이 됩니다."
                                              }
                                            }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(멘토 ID가 유효하지 않음)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                               "success": false,
                                               "message": "Mentor (id=2) 가 존재하지 않습니다.",
                                               "data": null
                                             }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "502", description = "Together API 응답 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseFormat.class,
                                    example = """
                                            {
                                              "success": false,
                                              "message": "TogetherAI API 응답이 올바르지 않습니다. Api Status : 404",
                                              "data": null
                                            }
                    """
                            )
                    )
            )
    })
    @PostMapping("/tag")
    public ResponseEntity<ApiResponseFormat<TagResponseDto>> generateMentorTag(
            @RequestBody CreateTagRequestDto dto) throws Exception {

        String prompt = aiService.createPrompt(dto.mentorId());

        TagResponseDto response = aiService.answer(prompt);

        aiService.updateMentorTag(dto.mentorId(), response.tag());

        return ResponseEntity.ok(ApiResponseFormat.ok(response));
    }
}
