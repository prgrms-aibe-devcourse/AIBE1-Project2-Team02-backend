package aibe1.proj2.mentoss.global.entity.enums;

public enum NotificationType {
    MESSAGE,        // 쪽지 수신
    APPLICATION,    // 내 강의에 누군가 신청
    RESPONSE;       // 내가 신청한 강의에 응답 (수락/거절)

    @Override
    public String toString() {
        return name();
    }

    public static boolean contains(String value) {
        for (NotificationType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}