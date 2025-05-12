package aibe1.proj2.mentoss.global.exception.report;

public class GetDataException extends RuntimeException {
    public GetDataException() {
        super("데이터를 불러오는 중 오류가 발생했습니다.");
    }
}
