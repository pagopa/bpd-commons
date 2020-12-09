package it.gov.pagopa.bpd.common.util;

public final class Constants {

    public static final long FUTURE_OR_PRESENT_TOLERANCE = 60;
    public static final String FISCAL_CODE_REGEX = "^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})|([0-9]{11})$";
    public static final String IBAN_REGEX = ".*^([iI][tT][0-9]{2}[a-zA-Z][0-9]{22}).*";
}
