package pl.akh.domainservicesvc.domain.services;

import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.repository.AdministratorRepository;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.services.api.DepartmentServiceImpl;
import pl.akh.model.rq.AddressRQ;
import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;
    private AdministratorRepository administratorRepository;
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    public void setUp() {
        if (this.departmentService == null) {
            this.departmentService = new DepartmentServiceImpl(departmentRepository, administratorRepository, Validation.buildDefaultValidatorFactory().getValidator());
        }
    }

    @Test
    public void shouldCreateDepartment() {
        //given
        DepartmentRQ departmentRQ = prepareDepartmentRQ();
        Department departmentReturned = new Department();
        departmentReturned.setId(1L);
        when(departmentRepository.save(any())).thenReturn(departmentReturned);

        //when
        DepartmentRS department = departmentService.createDepartment(departmentRQ);

        //then
        assertNotNull(department);
    }

    private DepartmentRQ prepareDepartmentRQ() {
        return DepartmentRQ.builder()
                .name("name")
                .addressRQ(prepareAddressRQ())
                .build();
    }

    private AddressRQ prepareAddressRQ() {
        return AddressRQ.builder()
                .city("Krakow")
                .country("PL")
                .postalCode("12-123")
                .post("Krakow Lobzow")
                .province("Malopolskie")
                .county("krakowski")
                .street("Wybickiego")
                .apartmentNumber("106")
                .houseNumber("56")
                .build();
    }
}