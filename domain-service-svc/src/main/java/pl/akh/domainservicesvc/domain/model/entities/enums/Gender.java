package pl.akh.domainservicesvc.domain.model.entities.enums;

public enum Gender {
    MALE, FEMALE;

    public pl.akh.model.common.Gender map() {
        return pl.akh.model.common.Gender.valueOf(this.name());
    }

    public static Gender valueOf(pl.akh.model.common.Gender gender) {
        return Gender.valueOf(gender.name());
    }
}
