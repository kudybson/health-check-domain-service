package pl.akh.model.common;

public enum Roles {
    PATIENT("ROLE_PATIENT"),
    RECEPTIONIST("ROLE_RECEPTIONIST"),
    DOCTOR("ROLE_DOCTOR"),
    ADMIN("ROLE_ADMIN"),
    SUPER_ADMIN("ROLE_SUPERADMIN");
    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
