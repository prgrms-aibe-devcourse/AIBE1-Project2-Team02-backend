package aibe1.proj2.mentoss.global.entity.enums;

public enum EntityStatus {
    AVAILABLE,
    SUSPENDED,
    BANNED;


    @Override
    public String toString() {
        return name();
    }
}