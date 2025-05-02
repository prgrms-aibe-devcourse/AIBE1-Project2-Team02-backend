package aibe1.proj2.mentoss.global.entity.enums;

public enum TargetType {
    USER,
    LECTURE,
    REVIEW;


    @Override
    public String toString() {
        return name();
    }
}