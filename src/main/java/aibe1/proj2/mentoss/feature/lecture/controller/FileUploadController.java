package aibe1.proj2.mentoss.feature.lecture.controller;

import aibe1.proj2.mentoss.feature.account.service.FileService;
import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/lectures/images")
public class FileUploadController {

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseFormat<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "lecture-images") String directory) {

        System.out.println("=== 파일 업로드 요청 받음 ===");
        System.out.println("파일명: " + file.getOriginalFilename());
        System.out.println("파일 크기: " + file.getSize());
        System.out.println("Content Type: " + file.getContentType());
        System.out.println("디렉토리: " + directory);

        try {
            System.out.println("파일 업로드 요청 받음: " + file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseFormat.fail("파일이 비어있습니다."));
            }

            String fileUrl = fileService.uploadFile(file, directory);
            return ResponseEntity.ok(ApiResponseFormat.ok(fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseFormat.fail("파일 업로드 실패: " + e.getMessage()));
        }
    }
}