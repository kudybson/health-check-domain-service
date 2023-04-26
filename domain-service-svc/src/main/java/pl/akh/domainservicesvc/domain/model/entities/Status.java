package pl.akh.domainservicesvc.domain.model.entities;

public enum Status {
    SCHEDULED, CANCELED, FINISHED;

    public pl.akh.model.common.Status toDto() {
        return pl.akh.model.common.Status.valueOf(this.name());
    }
}
