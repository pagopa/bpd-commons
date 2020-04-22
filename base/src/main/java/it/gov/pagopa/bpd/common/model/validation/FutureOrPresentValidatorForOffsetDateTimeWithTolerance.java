package it.gov.pagopa.bpd.common.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.OffsetDateTime;

//public class FutureOrPresentValidatorForOffsetDateTimeWithTolerance extends FutureOrPresentValidatorForOffsetDateTime {
public class FutureOrPresentValidatorForOffsetDateTimeWithTolerance
        implements ConstraintValidator<FutureOrPresentWithTolerance, OffsetDateTime> {

    private long tollerance;


    @Override
    public void initialize(FutureOrPresentWithTolerance constraintAnnotation) {
        tollerance = constraintAnnotation.tolerance();
    }


    @Override
    public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        final OffsetDateTime now = OffsetDateTime.now(context.getClockProvider().getClock());

        return value.isAfter(now.minus(Duration.ofMinutes(tollerance)));
    }

//    @Override
//    public void initialize(ConstraintDescriptor<FutureOrPresentWithTolerance> constraintDescriptor, HibernateConstraintValidatorInitializationContext initializationContext) {
//        tollerance = constraintDescriptor.getAnnotation().tolerance();
//        super.initialize(constraintDescriptor, initializationContext);
//    }
//
//    @Override
//    protected Duration getEffectiveTemporalValidationTolerance(Duration duration) {
//        return duration.plus(Duration.ofMinutes(tollerance)).negated();
//    }
//
//    @Override
//    protected OffsetDateTime getReferenceValue(Clock clock) {
//        return OffsetDateTime.now(clock);
//    }
//
//    @Override
//    protected boolean isValid(int result) {
//        return result >= 0;
//    }

}
