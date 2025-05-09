package aibe1.proj2.mentoss.global.exception.review;

public class TogetherApiException extends RuntimeException {
    private final int status;

    public TogetherApiException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}