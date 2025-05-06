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
import java.io.PrintWriter;
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

        log.debug("OAuth2User 속성: {}", oAuth2User.getAttributes());

        if ("google".equals(provider)) {
            handleGoogleLogin(oAuth2User, req, res);
        } else {
            handleKakaoLogin(oAuth2User, req, res);
        }
    }


    private void handleGoogleLogin(OAuth2User oAuth2User, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String provider = "google";
        String providerId = oAuth2User.getAttribute("sub");
        if (providerId == null) {
            log.error("Google providerId를 찾을 수 없습니다.");
            throw new IllegalStateException("Google 인증 정보를 찾을 수 없습니다.");
        }

        log.debug("Google providerId: {} (타입: {})", providerId, providerId.getClass().getName());

        // 공통 로직 호출
        processLogin(provider, providerId, req, res);
    }

    private void handleKakaoLogin(OAuth2User oAuth2User, HttpServletRequest req, HttpServletResponse res) throws IOException {

        String provider = "kakao";

        Object idObj = oAuth2User.getAttribute("id");
        if (idObj == null) {
            log.error("Kakao id를 찾을 수 없습니다");
            throw new IllegalStateException("Kakao 인증 정보를 찾을 수 없습니다");
        }

        log.debug("Kakao id(원본): {} (타입: {})", idObj, idObj.getClass().getName());

        String providerId = String.valueOf(idObj);
        log.debug("Kakao providerId(변환 후): {}", providerId);

        processLogin(provider, providerId, req, res);
    }


    private void processLogin(String provider, String providerId, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Optional<AppUser> userOpt = appUserMapper.findByProviderAndProviderId(provider, providerId);

        AppUser appUser = userOpt.get();
        String username = provider + "_" + providerId;

        log.debug("DB에서 조화한 사용자: userId={}, 타입={}",
                appUser.getUserId(),
                appUser.getUserId() != null ? appUser.getUserId().getClass().getName() : "null");

        Long userId = appUser.getUserId();
        if (userId == null) {
            log.warn("사용자 ID가 null입니다 기본값을 사용합니다");
            userId = 0L;
        }

        log.info("로그인 성공 : userId={}, provider={}, providerId={}", appUser.getUserId(), provider, providerId);

        try {
            String token = jwtTokenProvider.generateToken(
                    new UsernamePasswordAuthenticationToken(username, ""),
                    List.of("USER"),
                    userId
            );


            res.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = res.getWriter();

            String frontendOrigin;
            if ("dev".equals(activeProfile)) {
                frontendOrigin = "http://localhost:5173"; // 개발 환경
            } else {
                frontendOrigin = "https://mentoss.vercel.app"; // 배포 환경
            }

            String script = "<script>" +
                    "console.log('토큰 전송 시도');" +
                    "try {" +
                    "  window.opener.postMessage({ token: '" + token + "' }, '" + frontendOrigin + "');" +
                    "  console.log('토큰 전송 완료');" +
                    "} catch (e) {" +
                    "  console.error('토큰 전송 오류:', e);" +
                    "}" +
                    "window.close();" +
                    "</script>";

            writer.write(script);
            writer.flush();
        } catch (Exception e) {
            log.error("토큰 생성 중 오류 발생", e);
            throw e;
        }
    }
}
