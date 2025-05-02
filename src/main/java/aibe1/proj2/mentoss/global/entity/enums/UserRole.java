package aibe1.proj2.mentoss.global.entity.enums;

public enum UserRole {
    MENTEE,
    MENTOR,
    ADMIN;


    @Override
    public String toString() {
        return name();
    }
}