package aibe1.proj2.mentoss.feature.testController.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "TEST - R2 Storage API", description = "Cloudflare R2 연동 확인용 TEST API")
@RestController
@RequestMapping("/api/test/r2")
@RequiredArgsConstructor
public class R2TestController {

    private final S3Client r2Client;       // Config에서 주입
    private final String r2BucketName;     // Config에서 주입

    @Operation(
            summary = "R2에 파일 업로드",
            description = "Multipart 파일을 Cloudflare R2에 업로드합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "업로드 성공"),
                    @ApiResponse(responseCode = "400", description = "파일 없음", content = @Content)
            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return "파일이 비어 있습니다.";
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(r2BucketName)
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        r2Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
        return "✅ 파일 업로드 완료: " + file.getOriginalFilename();
    }

    @Operation(
            summary = "업로드된 파일 목록 조회",
            description = "R2 버킷에 업로드된 모든 파일명을 조회합니다.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "파일 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                    )
            )
    )
    @GetMapping("/list")
    public List<String> listObjects() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(r2BucketName)
                .build();

        return r2Client.listObjectsV2(request)
                .contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}