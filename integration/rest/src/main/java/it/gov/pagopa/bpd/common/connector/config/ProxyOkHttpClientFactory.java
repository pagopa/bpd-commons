package it.gov.pagopa.bpd.common.connector.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyOkHttpClientFactory extends DefaultOkHttpClientFactory {

    @Value("${rest-client.auth.proxy.host}")
    private String proxyHost;

    @Value("${rest-client.auth.proxy.port}")
    private Integer proxyPort;


    public ProxyOkHttpClientFactory(OkHttpClient.Builder builder) {
        super(builder);
    }

    @Override
    public OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
        OkHttpClient.Builder builder = super.createBuilder(true);
        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        return builder;
    }
}