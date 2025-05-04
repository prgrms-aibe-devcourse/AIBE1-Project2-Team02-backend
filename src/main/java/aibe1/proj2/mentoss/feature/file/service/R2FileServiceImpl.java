package aibe1.proj2.mentoss.feature.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class R2FileServiceImpl implements FileService {

    private final S3Client r2Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Override
    public String uploadFile(MultipartFile file, String directoryPath) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드 할 수 없습니다");
        }

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 저장 경로 및 파일명 생성 (UUID 사용)
        String folder = (directoryPath != null && !directoryPath.isEmpty()) ? directoryPath : "uploads";
        String fileName = UUID.randomUUID().toString() + extension;
        String key = folder + "/" + fileName;

        // S3 업로드 요청 생성
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        r2Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return publicUrl + "/" + key;
    }

    @Override
    public boolean deleteFile(String fileUrl) {

        if (fileUrl == null || !fileUrl.startsWith(publicUrl)) {
            return false;
        }

        String key;

        try {
            if (fileUrl.length() > publicUrl.length() + 1) {
                key = fileUrl.substring(publicUrl.length() + 1);
            } else {
                return false;
            }

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            r2Client.deleteObject(request);
            return true;
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
}
