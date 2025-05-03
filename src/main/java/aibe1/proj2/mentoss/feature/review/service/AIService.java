package aibe1.proj2.mentoss.feature.review.service;

public interface AIService {
    String answer(String prompt) throws Exception;

    String createPrompt(Long mentorId);

    void updateMentorTag(Long mentorId, String tag);
}
