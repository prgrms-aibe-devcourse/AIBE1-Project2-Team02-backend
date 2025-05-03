package aibe1.proj2.mentoss.global.exception;

public class ResourceAccessDeniedException extends RuntimeException {
    public ResourceAccessDeniedException(String resourceType, Long resourceId) {
        super(String.format("%s (id=%d)에 접근할 수 없습니다.", resourceType, resourceId));
    }
}