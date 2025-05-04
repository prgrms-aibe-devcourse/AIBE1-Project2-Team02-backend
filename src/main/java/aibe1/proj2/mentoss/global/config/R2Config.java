package aibe1.proj2.mentoss.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

/**
 * Cloudflare R2 연동을 위한 S3 클라이언트 설정 클래스
 * - Cloudflare R2는 AWS S3와 호환되므로 AWS SDK v2를 사용
 */
@Configuration
public class R2Config {

    // 🔐 환경 변수 또는 application.yml에서 주입
    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    /**
     * ☁️ Cloudflare R2용 S3Client Bean 등록
     * - endpointOverride: R2에서 발급받은 S3 호환 endpoint 사용
     * - pathStyleAccessEnabled: Cloudflare에서는 반드시 true 설정 필요
     */
    @Bean
    public S3Client r2Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint)) // 반드시 Cloudflare 제공 endpoint 사용
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // R2는 path-style만 지원
                        .build())
                .build();
    }

    /**
     * 📦 현재 버킷 이름을 주입받아 Bean으로 등록 (선택)
     */
    @Bean
    public String r2BucketName() {
        return bucket;
    }
}