package aibe1.proj2.mentoss.global.exception;

import aibe1.proj2.mentoss.global.dto.ApiResponseFormat;
import aibe1.proj2.mentoss.global.exception.review.InvalidRatingException;
import aibe1.proj2.mentoss.global.exception.review.NotAttendedLectureException;
import aibe1.proj2.mentoss.global.exception.review.NotOwnerException;
import aibe1.proj2.mentoss.global.exception.review.TogetherApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception : " + ex.getMessage());
    }

    /**
     * 엔티티를 찾을 수 없을 때의 예외 처리
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseFormat.fail(e.getMessage()));
    }

    /**
     * JSON 처리 오류 예외 처리
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleJsonProcessing(JsonProcessingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseFormat.fail("JSON 처리 중 오류가 발생했습니다: " + e.getMessage()));
    }

    /**
     * 외래키 참조 오류 (참조할 대상 ID가 존재하지 않을 때) 처리
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        String type = ex.getResourceType();
        String msg  = type + " (id=" + ex.getResourceId() + ") 가 존재하지 않습니다.";
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseFormat.fail(msg));
    }

    /**
     * 별점이 1~5 사이의 정수가 아닐 때의 오류 처리
     */
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleInvalidRating(InvalidRatingException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }

    /**
     * 특정 리소스 항목(유저, 강의, 후기 등)이 삭제되었거나 Available하지 않을 때 예외 처리
     */
    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleResourceAccessDenied(ResourceAccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }

    /**
     * 누락된 필드/잘못된 값 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }

    /**
     * TogetherAI LLM API 응답 오류 처리
     */
    @ExceptionHandler(TogetherApiException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleTogetherApiException(TogetherApiException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }

    /**
     * 수강하지 않은 강의에 대한 후기 작성 시
     */
    @ExceptionHandler(NotAttendedLectureException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleNotAttendedLecture(NotAttendedLectureException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }

    /**
     * 본인이 작성한 항목이 아닐 때 수정, 삭제 시도 시 예외
     */
    @ExceptionHandler(NotOwnerException.class)
    public ResponseEntity<ApiResponseFormat<Void>> handleNotReviewOwner(NotOwnerException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseFormat.fail(ex.getMessage()));
    }
}