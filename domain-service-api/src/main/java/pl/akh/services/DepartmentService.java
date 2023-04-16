package pl.akh.services;

import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;

import java.util.Collection;

public interface DepartmentService {
    DepartmentRS createDepartment(DepartmentRQ departmentRQ);

    Collection<DepartmentRS> getAllDepartments();

    Collection<DepartmentRS> getAllDepartments(Long pageNumber, Long pageSize);

    Collection<DepartmentRS> getDepartmentsByCriteria(String city, String street, String postalCode, String county,
                                                      String province, String country);

    DepartmentRS getDepartmentById(Long id);
}
