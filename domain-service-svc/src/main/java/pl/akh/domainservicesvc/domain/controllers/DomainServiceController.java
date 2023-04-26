package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.domain.services.AccessService;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;

public abstract class DomainServiceController {
    protected AuthDataExtractor authDataExtractor;
    private AccessService accessService;

    public DomainServiceController(AuthDataExtractor authDataExtractor, AccessService accessService) {
        this.authDataExtractor = authDataExtractor;
        this.accessService = accessService;
    }

    protected boolean hasAccessAdministrationAccessToDepartment(Long departmentId) {
        try {
            return accessService.hasAccessAdministrationAccessToDepartment(authDataExtractor.getId(), departmentId)
                    || authDataExtractor.getRoles().contains("ROLE_SUPERADMIN");
        } catch (AuthException e) {
            return false;
        }
    }

    protected boolean hasReceptionistAccessToDepartment(Long departmentId){
        try {
            return accessService.hasReceptionistAccessToDepartment(authDataExtractor.getId(), departmentId)
                    || authDataExtractor.getRoles().contains("ROLE_SUPERADMIN");
        } catch (AuthException e) {
            return false;
        }
    }
}
