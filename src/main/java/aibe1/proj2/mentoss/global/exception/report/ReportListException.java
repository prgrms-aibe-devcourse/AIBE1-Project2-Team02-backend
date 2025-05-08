package aibe1.proj2.mentoss.global.exception.report;

public class ReportListException extends RuntimeException {
    public ReportListException(Exception e) {
        super(e.getMessage());
    }
}
