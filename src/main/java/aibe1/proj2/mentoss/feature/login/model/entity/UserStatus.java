package aibe1.proj2.mentoss.feature.login.model.entity;

public enum UserStatus {
    AVAILABLE,
    SUSPENDED,
    BANNED;


    @Override
    public String toString() {
        return name();
    }
}
