package it.gov.pagopa.bpd.common.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public abstract class ResourceNotEnabledException extends MedaDomainRuntimeException {

    private static final String CODE = "resource.not-enabled.error";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;


    public <K extends Serializable> ResourceNotEnabledException(Class<?> resourceClass, K id) {
        super(getMessage(resourceClass.getSimpleName(), id), CODE, STATUS);
    }

    private static String getMessage(String resourceName, Object id) {
        return String.format("%s with id %s is not enabled", resourceName, id);
    }

    public <K extends Serializable> ResourceNotEnabledException(String resourceName, K id) {
        super(getMessage(resourceName, id), CODE, STATUS);
    }

}