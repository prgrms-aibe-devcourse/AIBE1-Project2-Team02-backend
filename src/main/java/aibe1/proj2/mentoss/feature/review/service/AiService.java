package aibe1.proj2.mentoss.feature.review.service;

import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;

public interface AiService {
    TagResponseDto answer(String prompt);

    String createPrompt(Long mentorId);

    void updateMentorTag(Long mentorId, String tag);
}
