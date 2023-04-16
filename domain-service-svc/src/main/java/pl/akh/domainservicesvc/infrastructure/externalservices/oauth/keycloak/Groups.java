package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

public enum Groups {
    PATIENT_GROUP("/PATIENT_GROUP"),

    RECEPTIONIST_GROUP("/RECEPTIONIST_GROUP"),

    DOCTOR_GROUP("/DOCTOR_GROUP"),

    ADMIN_GROUP("/ADMIN_GROUP"),

    SUPERADMIN_GROUP("/SUPERADMIN_GROUP");

    private final String group;

    Groups(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return this.group;
    }
}