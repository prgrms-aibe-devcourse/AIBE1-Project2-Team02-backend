package aibe1.proj2.mentoss.global.entity.enums;


public enum ActionType {
    FREE,
    WARN,
    SUSPEND,
    BAN;


    @Override
    public String toString() {
        return name();
    }
}