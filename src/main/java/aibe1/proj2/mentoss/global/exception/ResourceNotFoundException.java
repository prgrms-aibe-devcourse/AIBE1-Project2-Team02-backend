package aibe1.proj2.mentoss.global.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final Long resourceId;

    public ResourceNotFoundException(String resourceType, Long resourceId) {
        super(resourceType + " id=" + resourceId + " 가 존재하지 않습니다.");
        this.resourceType = resourceType;
        this.resourceId   = resourceId;
    }

    public String getResourceType() { return resourceType; }
    public Long   getResourceId()   { return resourceId;   }
}