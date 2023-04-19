package pl.akh.services;

import jdk.jfr.Description;
import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;

import java.util.Collection;

public interface DepartmentService {
    DepartmentRS createDepartment(DepartmentRQ departmentRQ);

    Collection<DepartmentRS> getAllDepartments();

    @Description(value = "no need to implement")
    Collection<DepartmentRS> getDepartmentsByCriteria(String city, String street, String county,
                                                      String province, String country);

    DepartmentRS getDepartmentById(Long id);

    DepartmentRS updateDepartment(DepartmentRQ departmentRQ, Long departmentId);

    void deleteDepartment(Long id);
}
