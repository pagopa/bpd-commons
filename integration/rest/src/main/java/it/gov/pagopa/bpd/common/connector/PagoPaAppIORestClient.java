package it.gov.pagopa.bpd.common.connector;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import it.gov.pagopa.bpd.common.connector.model.PagoPaAppIOAuthResource;


/**
 * PagoPa AppIO Rest Client
 */
@FeignClient(name = "${rest-client.appio.auth.serviceCode}", url = "${rest-client.appio.auth.base-url}")
public interface PagoPaAppIORestClient {

    @GetMapping(value = "${rest-client.appio.auth.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    PagoPaAppIOAuthResource authorizeBearerToken(@RequestHeader("Authorization") String apikey);
}
