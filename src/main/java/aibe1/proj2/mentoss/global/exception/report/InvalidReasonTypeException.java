package aibe1.proj2.mentoss.global.exception.report;

public class InvalidReasonTypeException extends RuntimeException {
    public InvalidReasonTypeException() {
        super("reasonType은 MESSAGE_CONTENT, LECTURE_CONTENT, REVIEW_CONTENT, APPLICATION_CONTENT, PROFILE 중 하나여야 합니다.");
    }
}