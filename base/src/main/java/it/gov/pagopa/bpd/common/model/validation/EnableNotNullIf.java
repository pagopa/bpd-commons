package it.gov.pagopa.bpd.common.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NotNullIfValidation.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface EnableNotNullIf {

    String message() default "{javax.validation.constraints.EnableNotNullIf.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
