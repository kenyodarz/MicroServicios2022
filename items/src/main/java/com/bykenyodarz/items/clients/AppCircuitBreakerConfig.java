package com.bykenyodarz.items.clients;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppCircuitBreakerConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(s -> new Resilience4JConfigBuilder(s)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10) // Tamaño de la ventana
                        .failureRateThreshold(50) // Tasa de falla por defecto 50%
                        .waitDurationInOpenState(Duration.ofSeconds(10L)) // Tiempo de abierto la ventana
                        // Vamos a trabajar ahora llamadas lentas
                        .slowCallRateThreshold(50)
                        .slowCallDurationThreshold(Duration.ofSeconds(4L))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(6L))// Timeout Limit(4L es normal)
                        .build())
                .build());
    }

}
