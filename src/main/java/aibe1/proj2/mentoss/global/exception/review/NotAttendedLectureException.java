package aibe1.proj2.mentoss.global.exception.review;

public class NotAttendedLectureException extends RuntimeException {
    public NotAttendedLectureException() {
        super("본인이 수강한 강의가 아닙니다.");
    }
}