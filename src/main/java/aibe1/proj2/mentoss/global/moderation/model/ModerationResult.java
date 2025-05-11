package aibe1.proj2.mentoss.global.moderation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 콘텐츠 유해성 검사 결과를 나타내는 모델 클래스입니다.
 * 콘텐츠가 차단되었는지 여부와 차단 사유를 포함합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationResult {

    /**
     * 콘텐츠가 차단되었는지 여부를 나타냅니다.
     */
    private boolean blocked;

    /**
     * 콘텐츠가 차단된 경우, 차단 사유를 나타냅니다.
     * 콘텐츠가 허용된 경우에는 null입니다.
     */
    private String reason;

    /**
     * 콘텐츠가 허용되었음을 나타내는 결과 객체를 생성합니다.
     *
     * @return 허용된 콘텐츠 결과를 나타내는 ModerationResult
     */
    public static ModerationResult allowed() {
        return ModerationResult.builder()
                .blocked(false)
                .reason(null)
                .build();
    }

    /**
     * 콘텐츠가 차단되었음을 나타내는 결과 객체를 생성합니다.
     *
     * @param reason 콘텐츠 차단 사유
     * @return 차단된 콘텐츠 결과를 나타내는 ModerationResult
     */
    public static ModerationResult blocked(String reason) {
        return ModerationResult.builder()
                .blocked(true)
                .reason(reason)
                .build();
    }
}