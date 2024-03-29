package it.gov.pagopa.bpd.common.model.validation;

import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class NotNullIfValidation implements ConstraintValidator<EnableNotNullIf, Object> {

    @Override
    public void initialize(EnableNotNullIf constraintAnnotation) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext ctx) {
        final Class<?> clazz = bean.getClass();
        final Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(NotNullIfPropertyEqualTo.class)) {
                if (field.get(bean) == null) {
                    for (NotNullIfPropertyEqualTo annotation :
                            field.getAnnotationsByType(NotNullIfPropertyEqualTo.class)) {
                        String dependencyField = annotation.property();
                        Object dependencyValue = clazz.getField(dependencyField).get(bean);

                        if (dependencyValue != null && !"".equals(dependencyValue)) {
                            String annotEqualsToValue = annotation.value();

                            if (annotEqualsToValue != null && !"".equals(annotEqualsToValue)
                                    && dependencyValue.equals(annotEqualsToValue)) {
                                return false;
//                            throw new NotNullIfException(field.getName(), dependencyField, annotEqualsToValue);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
