package it.gov.pagopa.bpd.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.BaseSpringTest;
import it.gov.pagopa.bpd.common.security.model.UserResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ContextConfiguration(classes = {
        JwtRestAuthenticationProvider.class
})
@RestClientTest(JwtRestAuthenticationProvider.class)
@TestPropertySource(properties = {
        "it.gov.pagopa.security.jwt.auth.url=/auth/test"
})
public class JwtRestAuthenticationProviderTest extends BaseSpringTest {

    @Autowired
    private JwtRestAuthenticationProvider jwtRestAuthenticationProvider;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockRestServiceServer.reset();
    }

    @Test
    public void testProvider_authenticate_Ok() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/auth/test")
                    .queryParam("token", "123456");
            this.mockRestServiceServer.expect(requestTo(builder.toUriString()))
                    .andRespond(withSuccess(
                            objectMapper.writeValueAsString(
                                    UserResponse.builder().fiscalCode("testCF").build())
                            , MediaType.APPLICATION_JSON));
            Authentication authentication = jwtRestAuthenticationProvider.authenticate(
                    new JwtAuthenticationToken("123456"));
            Assert.assertNotNull(authentication);
            Assert.assertEquals("testCF", authentication.getPrincipal());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testProvider_authenticate_NullParam() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/auth/test")
                    .queryParam("token", "null");
            this.mockRestServiceServer.expect(requestTo(builder.toUriString()))
                    .andRespond(withBadRequest());
            Authentication authentication = jwtRestAuthenticationProvider.authenticate(
                    new JwtAuthenticationToken(null));
            Assert.assertNull(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testProvider_authenticate_ServerError() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/auth/test")
                    .queryParam("token", "errorToken");
            this.mockRestServiceServer.expect(requestTo(builder.toUriString()))
                    .andRespond(withServerError());
            Authentication authentication = jwtRestAuthenticationProvider.authenticate(
                    new JwtAuthenticationToken("errorToken"));
            Assert.assertNull(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testProvider_authenticate_Unauthorized() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/auth/test")
                    .queryParam("token", "133131");
            this.mockRestServiceServer.expect(requestTo(builder.toUriString()))
                    .andRespond(withUnauthorizedRequest());
            Authentication authentication = jwtRestAuthenticationProvider.authenticate(
                    new JwtAuthenticationToken("133131"));
            Assert.assertNull(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testProvider_supports() {
        Assert.assertTrue(jwtRestAuthenticationProvider.supports(JwtAuthenticationToken.class));
        Assert.assertFalse(jwtRestAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

}