package it.gov.pagopa.bpd.common.connector.jpa;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ReadOnlyRepository {
}
