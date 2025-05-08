package aibe1.proj2.mentoss.feature.login.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/test")
@Tag(name = "TEST - Login API", description = "소셜 로그인 기능 확인용 TEST API")
public class LoginTestController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("이 엔드 포인트는 누구나 접근 가능합니다");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateEndpoint(Authentication authentication) {
        return ResponseEntity.ok("인증성공! 사용자 : " + authentication.getName());
    }
}
