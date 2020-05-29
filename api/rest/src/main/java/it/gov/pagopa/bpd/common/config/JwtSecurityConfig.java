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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    @Value("${it.gov.pagopa.security.patterns}")
    private String jwtAuthPatterns;

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

        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(new AntPathRequestMatcher(jwtAuthPatterns));
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers(jwtAuthPatterns).authenticated().and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authProvider);

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
