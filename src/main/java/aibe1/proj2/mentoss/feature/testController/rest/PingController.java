package aibe1.proj2.mentoss.feature.testController.rest;

import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Render 서버의 슬립 방지를 위한 Ping API 컨트롤러
 *
 * - GitHub Actions에서 주기적으로 이 API를 호출
 * - 일정 시간마다 호출되면 Render 서버가 슬립되지 않고 유지됨
 * - 프론트에서는 직접 사용하지 않으며, 내부 유지 목적의 엔드포인트
 */

@RestController
@Tag(name = "TEST - Ping API", description = "서버 지속 활성화용 Ping API")
public class PingController {

    @RequestMapping(value = "/api/ping", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ApiResponseFormat<String> ping() {
        return ApiResponseFormat.ok("pong");
    }
}