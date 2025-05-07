package aibe1.proj2.mentoss.feature.account.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadFile(MultipartFile file, String directoryPath) throws IOException;

    boolean deleteFile(String fileUrl);
}
