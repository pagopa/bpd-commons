package it.gov.pagopa.bpd.common.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpperCase {
}
