package aibe1.proj2.mentoss.feature.login.service;

import aibe1.proj2.mentoss.feature.login.model.mapper.AppUserMapper;
import aibe1.proj2.mentoss.global.auth.JwtTokenProvider;
import aibe1.proj2.mentoss.global.entity.AppUser;
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
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserMapper appUserMapper;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String provider = req.getRequestURI().contains("google") ? "google" : "kakao";

        log.debug("OAuth2User 속성: {}", oAuth2User.getAttributes());

        String providerId = extractProviderId(provider, oAuth2User);
        processLogin(provider, providerId, req, res);
    }

    private String extractProviderId(String provider, OAuth2User oAuth2User) {
        if ("google".equals(provider)) {
            String providerId = oAuth2User.getAttribute("sub");
            if (providerId == null) {
                log.error("Google providerId를 찾을 수 없습니다.");
                throw new IllegalStateException("Google 인증 정보를 찾을 수 없습니다.");
            }
            log.debug("Google providerId: {} (타입: {})", providerId, providerId.getClass().getName());
            return providerId;
        } else { // kakao
            Object idObj = oAuth2User.getAttribute("id");
            if (idObj == null) {
                log.error("Kakao id를 찾을 수 없습니다");
                throw new IllegalStateException("Kakao 인증 정보를 찾을 수 없습니다");
            }
            log.debug("Kakao id(원본): {} (타입: {})", idObj, idObj.getClass().getName());
            String providerId = String.valueOf(idObj);
            log.debug("Kakao providerId(변환 후): {}", providerId);
            return providerId;
        }
    }

    private void processLogin(String provider, String providerId, HttpServletRequest req, HttpServletResponse res) throws IOException {

        AppUser appUser = appUserMapper.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new IllegalStateException("소셜 로그인 사용자 정보를 찾을 수 없습니다: provider=" + provider + ", providerId=" + providerId));
        String username = provider + "_" + providerId;

        log.debug("DB에서 조회한 사용자: userId={}, 타입={}",
                appUser.getUserId(),
                appUser.getUserId() != null ? appUser.getUserId().getClass().getName() : "null");

        Long userId = appUser.getUserId();
        if (userId == null) {
            log.error("사용자 ID가 null입니다");
            throw new IllegalStateException("유효하지 않은 사용자 ID입니다");
        }

        log.info("로그인 성공 : userId={}, provider={}, providerId={}", appUser.getUserId(), provider, providerId);

        try {
            String token = jwtTokenProvider.generateToken(
                    new UsernamePasswordAuthenticationToken(username, ""),
                    List.of("USER"),
                    userId
            );

            // 리디렉션 URL 추출 (요청 파라미터에서)
            String redirectUri = "dev".equals(activeProfile)
                    ? "http://localhost:5173"  // 개발 환경
                    : "https://mentoss.vercel.app";

            // 리다이렉트 실행
            log.info("현재 프로필: {}, 리다이렉트 URI: {}", activeProfile, redirectUri);
            res.sendRedirect(redirectUri + "?token=" + token);
            log.debug("리다이렉트 완료: {}", redirectUri + "?token=" + token.substring(0, 10) + "...");

        } catch (Exception e) {
            log.error("토큰 생성 중 오류 발생", e);
            throw e;
        }
    }
}