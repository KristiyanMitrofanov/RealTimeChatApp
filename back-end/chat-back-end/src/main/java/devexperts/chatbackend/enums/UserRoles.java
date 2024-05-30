package devexperts.chatbackend.enums;

public enum UserRoles {

    USER,
    ADMIN;


    @Override
    public String toString() {
        switch (this) {
            case USER: return "USER";
            case ADMIN: return "ADMIN";
            default: return "";
        }
    }
}
