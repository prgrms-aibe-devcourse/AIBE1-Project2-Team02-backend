package aibe1.proj2.mentoss.global.exception;

/**
 * 엔티티를 찾을 수 없을 때 발생하는 예외
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}