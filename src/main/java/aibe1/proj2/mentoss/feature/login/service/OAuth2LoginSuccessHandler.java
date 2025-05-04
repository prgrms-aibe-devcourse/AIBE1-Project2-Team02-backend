package aibe1.proj2.mentoss.feature.login.service;

import aibe1.proj2.mentoss.feature.login.model.mapper.AppUserMapper;
import aibe1.proj2.mentoss.global.auth.JwtTokenProvider;
import aibe1.proj2.mentoss.global.entity.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final AppUserMapper appUserMapper;

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

        Optional<AppUser> userOpt = appUserMapper.findByProviderAndProviderId(provider, providerId);
        if(userOpt.isEmpty()) {
            log.error("소셜 로그인 사용자 정보가 DB에 존재하지 않습니다. provider={}, providerId={}", provider, providerId);
            throw new IllegalStateException("소셜 로그인 사용자 정보가 DB에 존재하지 않습니다");
        }

        AppUser appUser = userOpt.get();
        String username = provider + "_" + providerId;

        log.info("로그인 성공 : userId={}, provider={}, providerId={}", appUser.getUserId(), provider, providerId);

        String token = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(username, ""),
                List.of("USER"),
                appUser.getUserId()
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
