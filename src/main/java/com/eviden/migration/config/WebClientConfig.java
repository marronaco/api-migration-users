package com.eviden.migration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuracion de URL para acceder al
 * servicio REST de magento
 */
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient magentoWebClient(){
        return WebClient.builder()
                .baseUrl("http://tablerummagento.com/rest/V1")
                .defaultHeader("Content-Type","application/json")
                .build();

    }
}
