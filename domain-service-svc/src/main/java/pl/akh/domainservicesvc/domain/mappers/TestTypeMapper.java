package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.enums.TestType;

public class TestTypeMapper {

    public static TestType toEntity(pl.akh.model.common.TestType testType) {
        if (testType == null) return null;
        return TestType.valueOf(testType.toString());
    }

    public static pl.akh.model.common.TestType toDto(TestType testType) {
        if (testType == null) return null;
        return pl.akh.model.common.TestType.valueOf(testType.toString());
    }
}
