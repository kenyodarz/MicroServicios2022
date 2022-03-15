package com.bykenyodarz.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@Component
public class ExampleGlobalFilters implements GlobalFilter, Ordered {

    private static final String TOKEN_GENERATED = "token_generated";

    static final Logger LOGGER = LoggerFactory.getLogger(ExampleGlobalFilters.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var initialTime = LocalTime.now();
        for (String s : Arrays.asList(String.format("[%s] request enrutado a %s",
                        request.getMethod(), request.getURI()),
                "Global Pre Filter executed")) {
            LOGGER.info(s);
        }
        exchange.getRequest().mutate().headers(h -> h.add(TOKEN_GENERATED, "123456"));
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var finalTime = LocalTime.now();
            LOGGER.info("ejecutando filtro post");

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(TOKEN_GENERATED)).ifPresent(valor -> {
                exchange.getResponse().getHeaders().add(TOKEN_GENERATED, valor);
            });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
            LOGGER.info("Tiempo Total: -> {}", Duration.between(initialTime, finalTime).toMillis());
            LOGGER.info("Global Post Filter executed");
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
