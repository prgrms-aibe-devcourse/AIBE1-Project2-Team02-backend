package aibe1.proj2.mentoss.global.config;


import aibe1.proj2.mentoss.feature.login.service.CustomOAuth2UserService;
import aibe1.proj2.mentoss.feature.login.service.OAuth2LoginSuccessHandler;
import aibe1.proj2.mentoss.global.auth.JwtAuthenticationFilter;
import aibe1.proj2.mentoss.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    // 개발 환경용 설정
    @Bean
    @Profile("dev") // 개발 환경일 때만 이 빈이 활성화됨
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        // 개발 모드에서는 모든 요청 허용
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    // 배포 환경용 설정
    @Bean
    @Profile("!dev") // 개발 환경이 아닐 때 이 빈이 활성화됨
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        // JWT 인증 설정 (기존 코드와 동일)
        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 관리 방식 설정 (STATELESS = 세션 사용 안 함)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 공개 API 엔드포인트
                        .requestMatchers("/test/**", "/api/auth/test/public", "/oauth2/**", "/login/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // 관리자만 접근 가능한 엔드포인트
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 인증된 사용자만 접근 가능한 나머지 엔드포인트
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정 추가
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                // JWT 인증 필터 추가
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
}
