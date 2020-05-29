package it.gov.pagopa.bpd.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwtToken;

    public JwtAuthenticationToken(String jwtToken) {
        super((Collection)null);
        this.jwtToken = jwtToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String jwtToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwtToken = jwtToken;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }
}
