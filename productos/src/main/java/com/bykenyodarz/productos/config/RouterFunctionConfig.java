package com.bykenyodarz.productos.config;

import com.bykenyodarz.productos.handlers.ProductoHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

@Configuration
public class RouterFunctionConfig {

    private static final String PRODUCT = "/api/v1/productos";
    private static final String PRODUCT_ID = "/api/v1/productos/{id}";

    @Bean
    public RouterFunction<ServerResponse> routes(ProductoHandler handler) {
        return RouterFunctions.route(GET(PRODUCT), handler::listar)
               .andRoute(GET(PRODUCT_ID), handler::ver)
                .andRoute(POST(PRODUCT), handler::crear)
                .andRoute(PUT(PRODUCT_ID), handler::actualizar)
                .andRoute(DELETE(PRODUCT_ID), handler::eliminar)
                ;
    }
}
