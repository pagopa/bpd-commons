package it.gov.pagopa.bpd.common.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PagoPaAppIOAuthResource {

	private String name;
	
	@JsonProperty("family_name")
	private String familyName;
	
	@JsonProperty("fiscal_code")
	private String fiscalCode;
	
}
