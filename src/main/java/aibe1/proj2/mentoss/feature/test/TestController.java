package aibe1.proj2.mentoss.feature.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CORS 테스트용 공개 API
 */
@RestController
public class TestController {

    @GetMapping("/api/test/hello")
    public String helloTest() {
        return "CORS 설정이 잘 되었습니다!";
    }
}