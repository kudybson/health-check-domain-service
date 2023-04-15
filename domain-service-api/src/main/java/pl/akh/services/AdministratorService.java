package pl.akh.services;

import pl.akh.model.rq.admin.CreateAdministratorRQ;
import pl.akh.model.rs.AdministratorRS;

public interface AdministratorService {
    AdministratorRS addAdministrator(CreateAdministratorRQ createAdministratorRQ);
}
