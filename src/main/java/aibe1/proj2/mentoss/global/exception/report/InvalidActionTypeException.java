package aibe1.proj2.mentoss.global.exception.report;

public class InvalidActionTypeException extends RuntimeException {
    public InvalidActionTypeException() {
        super("actionType은 FREE, WARN, SUSPEND, BAN 중 하나여야 합니다.");
    }
}
