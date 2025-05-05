package aibe1.proj2.mentoss.global.exception.review;

public class InvalidRatingException extends RuntimeException {
    public InvalidRatingException(String msg) {
        super(msg);
    }
}