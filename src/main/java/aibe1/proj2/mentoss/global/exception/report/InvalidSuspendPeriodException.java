package aibe1.proj2.mentoss.global.exception.report;

public class InvalidSuspendPeriodException extends RuntimeException {
    public InvalidSuspendPeriodException() {
        super("Action이 FREE나 WARN일 경우 제재일은 0일이어야 합니다.");
    }
}
