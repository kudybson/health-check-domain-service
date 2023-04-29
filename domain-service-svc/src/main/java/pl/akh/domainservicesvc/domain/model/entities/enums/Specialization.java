package pl.akh.domainservicesvc.domain.model.entities.enums;

public enum Specialization {
    ANESTHESIA,
    CARDIOVASCULAR,
    COMMUNITYHEALTH,
    DENTISTRY,
    DERMATOLOGY,
    DIETNUTRITION,
    EMERGENCY,
    ENDOCRINE,
    GASTROENTEROLOGIC,
    GENETIC,
    GERIATRIC,
    GYNECOLOGIC,
    HEMATOLOGIC,
    INFECTIOUS,
    LABORATORYSCIENCE,
    MIDWIFERY,
    MUSCULOSKELETAL,
    NEUROLOGIC,
    NURSING,
    OBSTETRIC,
    ONCOLOGIC,
    OPTOMETRIC,
    OTOLARYNGOLOGIC,
    PATHOLOGY,
    PEDIATRIC,
    PHARMACYSPECIALTY,
    PHYSIOTHERAPY,
    PLASTICSURGERY,
    PODIATRIC,
    PRIMARYCARE,
    PSYCHIATRIC,
    PUBLICHEALTH,
    PULMONARY,
    RADIOGRAPHY,
    RENAL,
    RESPIRATORYTHERAPY,
    RHEUMATOLOGIC,
    SPEECHPATHOLOGY,
    SURGICAL,
    TOXICOLOGIC,
    UROLOGIC;

    public pl.akh.model.common.Specialization toDto() {
        return pl.akh.model.common.Specialization.valueOf(this.name());
    }
}
