package aibe1.proj2.mentoss.feature.account.model.dto;

import org.springframework.web.multipart.MultipartFile;

public record MentorProfileRequestDto (
        String content,
        MultipartFile appealFile
) {
}
