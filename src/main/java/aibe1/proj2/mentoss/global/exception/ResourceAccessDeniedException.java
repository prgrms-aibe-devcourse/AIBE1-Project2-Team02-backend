package aibe1.proj2.mentoss.global.exception;

public class ResourceAccessDeniedException extends RuntimeException {
    public ResourceAccessDeniedException(String resourceType, Long resourceId) {
        super(String.format("%s (id=%d) 항목이 삭제되었거나 정지 상태입니다.", resourceType, resourceId));
    }
}