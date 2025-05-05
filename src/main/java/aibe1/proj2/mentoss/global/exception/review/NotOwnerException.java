package aibe1.proj2.mentoss.global.exception.review;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException() {
        super("본인이 작성한 항목이 아닙니다.");
    }
}
