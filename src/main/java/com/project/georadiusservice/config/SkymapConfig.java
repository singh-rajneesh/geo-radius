package com.project.georadiusservice.config;

import com.climate.client.SkymapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class SkymapConfig {

    private final RestTemplate restTemplate;

    @Value("${skymap.url}")
    private String skymapUrl;
    @Value("${skymap.api.key}")
    private String skymapApiKey;

    @Bean
    public SkymapClient skymapClient() {
        return new SkymapClient(restTemplate, skymapUrl, skymapApiKey);
    }
}
