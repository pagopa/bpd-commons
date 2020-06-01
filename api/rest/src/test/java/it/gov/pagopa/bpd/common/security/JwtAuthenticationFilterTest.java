package it.gov.pagopa.bpd.common.security;

import eu.sia.meda.BaseTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtAuthenticationFilterTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNormalOperation() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
        request.addHeader("Authorization", "Bearer 123456");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new AntPathRequestMatcher("/**"));
        filter.setAuthenticationManager(createAuthenticationManager());

        Authentication result = filter.attemptAuthentication(request,
                new MockHttpServletResponse());
        assertThat(result != null).isTrue();
    }

    @Test
    public void testNullHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new AntPathRequestMatcher("/**"));
        filter.setAuthenticationManager(createAuthenticationManager());
        expectedException.expect(AuthenticationCredentialsNotFoundException.class);
        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test
    public void testVoidAuthorizationHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
        request.addHeader("Authorization", "");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new AntPathRequestMatcher("/**"));
        filter.setAuthenticationManager(createAuthenticationManager());
        expectedException.expect(AuthenticationCredentialsNotFoundException.class);
        filter.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test
    public void noSessionIsCreatedIfAllowSessionCreationIsFalse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.addHeader("Authorization", "Bearer 123456");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new AntPathRequestMatcher("/**"));

        filter.setAllowSessionCreation(false);
        filter.setAuthenticationManager(createAuthenticationManager());

        filter.attemptAuthentication(request, new MockHttpServletResponse());

        assertThat(request.getSession(false)).isNull();
    }

    private AuthenticationManager createAuthenticationManager() {
        AuthenticationManager am = mock(AuthenticationManager.class);
        when(am.authenticate(any(Authentication.class))).thenAnswer(
                (Answer<Authentication>) invocation -> (Authentication) invocation.getArguments()[0]);
        return am;
    }

}