package aibe1.proj2.mentoss.global.entity.enums;

public enum ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;


    @Override
    public String toString() {
        return name();
    }

    public static boolean contains(String value) {
        for (var t : values()) {
            if (t.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}