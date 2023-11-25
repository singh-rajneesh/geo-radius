package com.project.georadiusservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {

    @Value("${webclient.http.connection.timeout.millisec}")
    private Integer connectionTimeoutMillis;

    @Value("${webclient.http.read.timeout.seconds}")
    private Integer readTimeout;

    @Value("${webclient.http.write.timeout.seconds}")
    private Integer writeTimeout;

    @Value("${farmrise.user.service.host}")
    private String userServiceServerUrl;

    @Value("${farmrise.agronomy.service.host}")
    private String agronomyServiceUrl;

    @Value("${advisor.service.host}")
    private String advisorServiceUrl;

    @Value("${google.maps.service.host}")
    private String googleMapsServerUrl;

    @Value("${azure.client.url}")
    private String azureClientUrl;

    @Value("${farmrise.whatsapp.service.host}")
    private String whatsappServiceHost;

    @Value("${farmrise.translation.service.host}")
    private String translationServiceHost;

    private HttpClient getHttpClient() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        return HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMillis)
                                .doOnConnected(conn -> conn
                                        .addHandlerLast(new ReadTimeoutHandler(readTimeout))
                                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout))))
                .secure(t -> t.sslContext(sslContext));
    }

    @Bean(name = "googleMapsWebClient")
    public WebClient googleMapsClientConfig() throws SSLException {
        return WebClient.builder()
                .baseUrl(googleMapsServerUrl)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "userServiceWebClient")
    public WebClient userServiceClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(userServiceServerUrl)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "agronomyServiceWebClient")
    public WebClient agronomyServiceClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(agronomyServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "advisorWebClient")
    public WebClient advisorClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(advisorServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "azureWebClient")
    public WebClient azureClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(azureClientUrl)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "whatsappWebClient")
    public WebClient whatsappClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(whatsappServiceHost)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    @Bean(name = "translationClient")
    public WebClient translationClient() throws SSLException {
        return WebClient.builder()
                .baseUrl(translationServiceHost)
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .filter(logRequest())
                .filter(logResponseStatus())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Request: {} {} {}", request.method(), request.url(), request.body());
            request.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction logResponseStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Response Status: {}", response.statusCode());
            return Mono.just(response);
        });
    }
}