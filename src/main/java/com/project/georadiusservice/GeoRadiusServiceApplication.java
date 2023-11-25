package com.project.georadiusservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"com.farmrise.georadiusservice"}, exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class, ContextRegionProviderAutoConfiguration.class})
public class GeoRadiusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoRadiusServiceApplication.class, args);
    }

}