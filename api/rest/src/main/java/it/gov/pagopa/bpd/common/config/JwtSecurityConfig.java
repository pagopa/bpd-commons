package it.gov.pagopa.bpd.common.config;

import it.gov.pagopa.bpd.common.security.JwtAuthenticationFilter;
import it.gov.pagopa.bpd.common.security.JwtRestAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty("it.gov.pagopa.security.enabled")
@PropertySource("classpath:config/security-config.properties")
@Order(1)
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${connectors.jpaConfigurations.connection.mocked:false}")
    private boolean jpaMocked;
    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;
    @Value("#{'${it.gov.pagopa.security.patterns}'.split(',')}")
    private List<String> jwtAuthPatterns;

    private final JwtRestAuthenticationProvider authProvider;

    /**
     * Configure.
     *
     * @param http the http
     * @throws Exception the exception
     */
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();

        HttpSecurityBuilder httpSecurityBuilder =
                http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers(jwtAuthPatterns.toArray(new String[jwtAuthPatterns.size()])).authenticated().and();

        for (String jwtAuthPattern : jwtAuthPatterns) {
            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(new AntPathRequestMatcher(jwtAuthPattern));
            jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
            httpSecurityBuilder = httpSecurityBuilder
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        httpSecurityBuilder.authenticationProvider(authProvider);

        if(jpaMocked && h2ConsoleEnabled){
            http.headers().frameOptions().disable();
        }

    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
