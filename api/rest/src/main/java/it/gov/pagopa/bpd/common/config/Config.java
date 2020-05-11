package it.gov.pagopa.bpd.common.config;

import eu.sia.meda.config.BaseInterceptorConfiguration;
import it.gov.pagopa.bpd.common.formatter.UpperCaseFormatterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(BaseInterceptorConfiguration.class)
public class Config implements WebMvcConfigurer {


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new UpperCaseFormatterFactory());
    }
}
