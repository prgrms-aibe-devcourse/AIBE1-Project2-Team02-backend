package aibe1.proj2.mentoss.global.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {
        super("데이터베이스 처리 중 문제가 발생했습니다.");
    }
}
