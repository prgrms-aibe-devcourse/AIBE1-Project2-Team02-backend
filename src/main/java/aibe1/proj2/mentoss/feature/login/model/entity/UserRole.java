package aibe1.proj2.mentoss.feature.login.model.entity;

public enum UserRole {
    MENTEE,
    MENTOR,
    ADMIN;


    @Override
    public String toString() {
        return name();
    }
}
