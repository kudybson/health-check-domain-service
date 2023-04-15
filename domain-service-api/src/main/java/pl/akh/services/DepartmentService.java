package pl.akh.services;

import jakarta.validation.constraints.NotEmpty;
import pl.akh.model.rq.department.CreateDepartmentRQ;
import pl.akh.model.rs.DepartmentRS;

import java.util.Collection;

public interface DepartmentService {
    DepartmentRS createDepartment(CreateDepartmentRQ createDepartmentRQ);

    Collection<DepartmentRS> getAllDepartments();

    Collection<DepartmentRS> getAllDepartments(Long pageNumber, Long pageSize);

    Collection<DepartmentRS> getDepartmentsByCriteria(String city, String street, String postalCode, String county,
                                                      String province, String country);

    DepartmentRS getDepartmentById(Long id);
}
