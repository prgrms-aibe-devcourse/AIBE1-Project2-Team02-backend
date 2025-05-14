package aibe1.proj2.mentoss.feature.review.controller;


import aibe1.proj2.mentoss.feature.review.model.dto.TagResponseDto;
import aibe1.proj2.mentoss.feature.review.model.mapper.AiMapper;
import aibe1.proj2.mentoss.feature.review.service.AiService;
import aibe1.proj2.mentoss.global.entity.MentorProfile;
import aibe1.proj2.mentoss.global.exception.review.TogetherApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiScheduler {
    private final AiMapper aiMapper;
    private final AiService aiService;
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    //@Scheduled(initialDelay = 1000, fixedRate = 24 * 60 * 60 * 1000)
    public void generateTagsForAllMentors() {
        List<MentorProfile> allMentor = aiMapper.findAllMentor();

        for (MentorProfile m : allMentor) {
            try {
                String prompt = aiService.createPrompt(m.getMentorId());

                TagResponseDto dto = aiService.answer(prompt);

                aiService.updateMentorTag(m.getMentorId(), dto.tag());
            } catch (TogetherApiException tae) {
                if (tae.getStatus() == 429) {
                    log.warn("🔔 레이트 리밋 감지 — 다음 대상으로 넘깁니다.");
                    continue;
                } else {
                    log.error("❌ 멘토[{}] 오류", m.getMentorId(), tae);
                }
            } catch (Exception e) {
                log.error("❌ 멘토[{}] 예기치 못한 오류", m.getMentorId(), e);
            }
            try {
                Thread.sleep(2_000);  // 2초 대기
            } catch (InterruptedException ignored) {}
        }
        log.info("✅ 태그 자동 생성 스케줄러 종료");

    }
}
