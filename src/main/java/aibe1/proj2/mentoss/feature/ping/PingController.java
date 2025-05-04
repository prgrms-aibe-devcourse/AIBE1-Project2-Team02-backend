package aibe1.proj2.mentoss.feature.ping;

import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Render 서버의 슬립 방지를 위한 Ping API 컨트롤러
 *
 * - GitHub Actions에서 주기적으로 이 API를 호출
 * - 일정 시간마다 호출되면 Render 서버가 슬립되지 않고 유지됨
 * - 프론트에서는 직접 사용하지 않으며, 내부 유지 목적의 엔드포인트
 */

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public ApiResponseFormat<String> ping() {
        return ApiResponseFormat.ok("pong");
    }
}