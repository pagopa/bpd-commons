package it.gov.pagopa.bpd.common.connector.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import it.gov.pagopa.bpd.common.connector.interceptor.QueryParamsPlusEncoderInterceptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@PropertySource("classpath:config/rest-client.properties")
public class RestConnectorConfig {

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    public RequestInterceptor queryParamsPlusEncoderInterceptor() {
        return new QueryParamsPlusEncoderInterceptor();
    }


    @Bean
    public Decoder feignDecoder() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }


    @Bean
    public Encoder feignEncoder() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new SpringEncoder(objectFactory);
    }

}
