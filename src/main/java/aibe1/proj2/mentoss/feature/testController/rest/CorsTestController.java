package aibe1.proj2.mentoss.feature.testController.rest;

import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CORS 테스트용 공개 API
 */
@RestController
@Tag(name = "TEST - Cors 테스트 API", description = "Cors 설정 테스트용 TEST API")
public class CorsTestController {

    @GetMapping("/api/test/hello")
    public ApiResponseFormat<String> helloTest() {
        return ApiResponseFormat.ok("CORS 설정이 잘 되었습니다!");
    }
}