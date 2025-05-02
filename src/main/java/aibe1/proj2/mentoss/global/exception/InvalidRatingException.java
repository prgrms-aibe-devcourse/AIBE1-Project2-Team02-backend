package aibe1.proj2.mentoss.global.exception;

public class InvalidRatingException extends RuntimeException {
    public InvalidRatingException(String msg) {
        super(msg);
    }
}