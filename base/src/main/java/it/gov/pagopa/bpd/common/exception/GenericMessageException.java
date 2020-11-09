package it.gov.pagopa.bpd.common.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public abstract class GenericMessageException extends MedaDomainRuntimeException {

    private static final String CODE = "generic.error";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;


    public <K extends Serializable> GenericMessageException(String message) {
        super(getMessage(message), CODE, STATUS);
    }

    private static String getMessage(String message) {
        return String.format(message);
    }

}
