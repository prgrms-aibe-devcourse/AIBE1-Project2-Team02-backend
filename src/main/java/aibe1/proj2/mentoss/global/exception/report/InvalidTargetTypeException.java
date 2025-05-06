package aibe1.proj2.mentoss.global.exception.report;

public class InvalidTargetTypeException extends RuntimeException {
    public InvalidTargetTypeException() {
        super("targetType은 USER, LECTURE, REVIEW 중 하나여야 합니다.");
    }
}