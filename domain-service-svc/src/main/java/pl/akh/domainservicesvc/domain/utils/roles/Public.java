package pl.akh.domainservicesvc.domain.utils.roles;

import jdk.jfr.Description;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Description(value = "Public annotation indicates which endpoints should be public.")
public @interface Public {
}
