package it.gov.pagopa.bpd.common.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResourceNotFoundException extends MedaDomainRuntimeException {

    private static final String CODE = "resource.not-found.error";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;


    public <K extends Serializable> ResourceNotFoundException(Class<?> resourceClass, K id) {
        this(getMessage(resourceClass.getSimpleName(), id));
    }

    public <K extends Serializable> ResourceNotFoundException(String resourceName, K id) {
        super(getMessage(resourceName, id), CODE, STATUS);
    }

    public ResourceNotFoundException(String message) {
        super(message, CODE, STATUS);
    }

    private static String getMessage(String resourceName, Object id) {
        return String.format("%s with id %s is not enabled", resourceName, id);
    }
}

