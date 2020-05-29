package it.gov.pagopa.bpd.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${it.gov.pagopa.security.jwt.auth.url}")
    private String jwtAuthUrl;

    @Override
    public Authentication authenticate (Authentication authentication)
      throws AuthenticationException {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String token = String.valueOf(authentication.getPrincipal());
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(jwtAuthUrl)
                    .queryParam("token", token);
            String uriBuilder = builder.build().encode().toUriString();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriBuilder, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return new UsernamePasswordAuthenticationToken(
                        responseEntity.getBody(), null, new ArrayList<>());
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