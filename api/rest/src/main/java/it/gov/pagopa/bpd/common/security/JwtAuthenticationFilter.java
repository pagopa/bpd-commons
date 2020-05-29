package it.gov.pagopa.bpd.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");

    public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException {

        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);

            if (matcher.matches()) {
               token = matcher.group("token");
                return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
            }

            throw new AuthenticationCredentialsNotFoundException("Token not found in Authorization Header");

        }

        throw new AuthenticationCredentialsNotFoundException("Authorization Header not found");

    }


}
