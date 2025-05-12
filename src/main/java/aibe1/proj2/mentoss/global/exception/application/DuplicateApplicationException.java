package aibe1.proj2.mentoss.global.exception.application;

public class DuplicateApplicationException extends RuntimeException{
    public DuplicateApplicationException(String message) {
        super(message);
    }
}
