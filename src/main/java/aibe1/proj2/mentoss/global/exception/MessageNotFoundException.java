package aibe1.proj2.mentoss.global.exception;

/**
 * 특정 ID의 메시지를 찾을 수 없을 때 발생하는 예외
 */
public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long id) {
        super("ID " + id + "번 쪽지를 찾을 수 없습니다.");
    }
}