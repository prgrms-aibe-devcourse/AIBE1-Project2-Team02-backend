package aibe1.proj2.mentoss.global.entity.enums;

public enum ReportReasonType {
    MESSAGE_CONTENT,
    LECTURE_CONTENT,
    REVIEW_CONTENT,
    APPLICATION_CONTENT,
    PROFILE;

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
