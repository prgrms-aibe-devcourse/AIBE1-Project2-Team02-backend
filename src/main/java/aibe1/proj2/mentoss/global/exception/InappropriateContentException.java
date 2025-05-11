package aibe1.proj2.mentoss.global.exception;

/**
 * 콘텐츠 유해성 검사 중 부적절한 내용이 감지되었을 때 발생하는 예외입니다.
 */
public class InappropriateContentException extends RuntimeException {
    private final String reason;

    /**
     * 부적절한 콘텐츠로 판단된 사유를 포함하여 예외를 생성합니다.
     *
     * @param reason 콘텐츠가 부적절하다고 판단된 이유
     */
    public InappropriateContentException(String reason) {
        super("Inappropriate content detected: " + reason);
        this.reason = reason;
    }

    /**
     * 콘텐츠가 부적절하다고 판단된 이유를 반환합니다.
     *
     * @return 차단 사유 문자열
     */
    public String getReason() {
        return reason;
    }
}