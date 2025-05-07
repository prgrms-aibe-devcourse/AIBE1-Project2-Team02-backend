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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    /**
     * CORS 설정
     * - 프론트엔드 도메인에서 백엔드 API에 접근 가능하도록 허용
     * - localhost:5173 (개발), mentoss.vercel.app (배포) 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://mentoss.vercel.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // 쿠키 및 인증 정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 개발 환경용 설정
    @Bean
    @Profile("dev") // 개발 환경일 때만 이 빈이 활성화됨
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        // 개발 모드에서는 모든 요청 허용
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .csrf(AbstractHttpConfigurer::disable);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                // OAuth2 로그인 설정 추가
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                );
        return http.build();
    }

    // 배포 환경용 설정
    @Bean
    @Profile("!dev") // 개발 환경이 아닐 때 이 빈이 활성화됨
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        // JWT 인증 설정 (기존 코드와 동일)
        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 관리 방식 설정 (STATELESS = 세션 사용 안 함)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 공개 API 엔드포인트
                        .requestMatchers("/test/**", "/api/test/**", "/api/auth/test/public", "/oauth2/**", "/login/**", "/css/**", "/js/**", "/images/**", "/api/ping", "/api/categories/**", "/api/regions/**", "/api/lectures/**").permitAll()
                        // 관리자만 접근 가능한 엔드포인트
                        .requestMatchers("/api/admin/**", "/adminPage").hasRole("ADMIN")
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