package aibe1.proj2.mentoss.global.exception;

public class DuplicateReportException extends RuntimeException{
    public DuplicateReportException() {
        super("이미 이 대상을 신고하셨습니다.");
    }
}