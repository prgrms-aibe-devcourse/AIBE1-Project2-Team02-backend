package aibe1.proj2.mentoss.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션 공통 설정
 */
@Configuration
public class AppConfig {

    /**
     * LocalDateTime, LocalDate, ZonedDateTime 날짜/시간 API(LocalDateTime 등)를 제대로 처리하기 위해 JavaTimeModule 등록
     * ObjectMapper 빈 설정
     * - JSON 직렬화/역직렬화에 사용
     * - JavaTimeModule 추가하여 LocalDateTime 등 Java 8 시간 API 지원
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}