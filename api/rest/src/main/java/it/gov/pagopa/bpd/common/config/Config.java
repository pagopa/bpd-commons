package it.gov.pagopa.bpd.common.config;

import eu.sia.meda.config.BaseInterceptorConfiguration;
import it.gov.pagopa.bpd.common.formatter.UpperCaseFormatterFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.gov.pagopa.bpd.common.connector.interceptor.AuthAppIoInterceptor;

@Configuration
@Import(BaseInterceptorConfiguration.class)
public class Config implements WebMvcConfigurer {

	@Autowired
    private AuthAppIoInterceptor authAppioInterceptor;
    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(authAppioInterceptor);
	}
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new UpperCaseFormatterFactory());
    }
}
