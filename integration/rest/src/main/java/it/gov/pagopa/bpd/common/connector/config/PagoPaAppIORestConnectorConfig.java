package it.gov.pagopa.bpd.common.connector.config;

import it.gov.pagopa.bpd.common.connector.PagoPaAppIORestClient;
import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = PagoPaAppIORestClient.class)
@PropertySource("classpath:config/rest-client.properties")
@Slf4j
public class PagoPaAppIORestConnectorConfig {

    @Configuration
    @ConditionalOnClass(OkHttpClient.class)
    static class OkHttpClientConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public OkHttpClientFactory okHttpClientFactory(OkHttpClient.Builder builder) {
            return new ProxyOkHttpClientFactory(builder);
        }
    }
}
