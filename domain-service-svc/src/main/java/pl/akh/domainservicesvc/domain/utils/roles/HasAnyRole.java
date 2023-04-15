package pl.akh.domainservicesvc.domain.utils.roles;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@PreAuthorize("hasAnyAuthority('ROLE_PATIENT','ROLE_DOCTOR','ROLE_RECEPTIONIST','ROLE_ADMIN','ROLE_SUPERADMIN')")
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAnyRole {
}
