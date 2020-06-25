package it.gov.pagopa.bpd.common.security;

import it.gov.pagopa.bpd.common.security.model.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@Component
@Slf4j
public class JwtRestAuthenticationProvider implements AuthenticationProvider {

    @Value("${it.gov.pagopa.security.jwt.auth.url:#{null}}")
    private String jwtAuthUrl;

    private final RestTemplate restTemplate;

    public JwtRestAuthenticationProvider(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Authentication authenticate (Authentication authentication)
      throws AuthenticationException {

        try {
            String token = String.valueOf(authentication.getPrincipal());
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(jwtAuthUrl)
                    .queryParam("token", token);
            String uriBuilder = builder.build().encode().toUriString();
            ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity(uriBuilder, UserResponse.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return new UsernamePasswordAuthenticationToken(
                        responseEntity.getBody().getFiscalCode(), null, new ArrayList<>());
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication){
        return authentication.equals(JwtAuthenticationToken.class);
    }

}