package aibe1.proj2.mentoss.feature.login.service;

import aibe1.proj2.mentoss.global.auth.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String provider = req.getRequestURI().contains("google")? "google" : "kakao";

        String providerId;
        if ("google".equals(provider)) {
            providerId = oAuth2User.getAttribute("sub");
        } else {
            providerId = String.valueOf(oAuth2User.getAttribute("id"));
        }

        String username = provider + "_" + providerId;

        String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(username, ""), List.of("USER")
        );

        // 개발 환경에서는 테스트 페이지로 리다이렉트
        if ("dev".equals(activeProfile)) {
            String redirectUrl = "/test/login?token=" + token;
            res.sendRedirect(redirectUrl);
        }
        // 운영 환경에서는 JSON 응답
        else {
            res.setContentType("application/json;charset=UTF-8");
            Map<String, String> result = Map.of("token", token);
            res.getWriter().write(objectMapper.writeValueAsString(result));
        }
    }
}
