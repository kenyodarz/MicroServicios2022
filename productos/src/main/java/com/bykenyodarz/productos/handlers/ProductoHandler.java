package com.bykenyodarz.productos.handlers;

import com.bykenyodarz.productos.models.Producto;
import com.bykenyodarz.productos.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class ProductoHandler {

    private final IProductService service;
    private final Validator validator;

    private String path;

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(service.findAllProductos(), Producto.class);
    }

    @SneakyThrows
    public Mono<ServerResponse> ver(ServerRequest request) {
        var id = request.pathVariable("id");
        if ("10".equals(id)) {
            throw new IllegalStateException("Producto no Encontrado");
        }
        if ("20".equals(id)) {
            return service.findAllProductos().next()
                    .flatMap(producto ->
                            ServerResponse.ok().body(BodyInserters.fromValue(producto)))
                    .delayElement(Duration.ofSeconds(5L))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }
        return service.findById(id).flatMap(producto ->
                        ServerResponse.ok().body(BodyInserters.fromValue(producto)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        var producto = request.bodyToMono(Producto.class);
        return producto.flatMap(p -> {
            //Validar Errores
            Errors errors = new BeanPropertyBindingResult(p, Producto.class.getName());
            validator.validate(p, errors);
            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> String.format("El campo %s %s",
                                fieldError.getField(), fieldError.getDefaultMessage()))
                        .collectList()
                        .flatMap(strings -> ServerResponse.badRequest().body(BodyInserters.fromValue(strings)));
            }// fin de la validacion
            else {
                if (p.getCreatedAt() == null) p.setCreatedAt(LocalDateTime.now());
                return service.save(p).flatMap(pdb ->
                        ServerResponse
                                .created(URI.create("/api/v1/productos/".concat(pdb.getIdProducto())))
                                .contentType(APPLICATION_JSON)
                                .body(BodyInserters.fromValue(pdb)));
            }
        });
    }

    public Mono<ServerResponse> actualizar(ServerRequest request) {
        var id = request.pathVariable("id");
        var producto = request.bodyToMono(Producto.class);
        var productoBD = service.findById(id);

        return productoBD.zipWith(producto, (pdb, req) -> {
                    pdb.setNombre(req.getNombre());
                    pdb.setPrecio(req.getPrecio());
                    pdb.setCreatedAt(req.getCreatedAt());
                    return pdb;
                }).flatMap(p -> ServerResponse
                        .created(URI.create("/api/v1/productos".concat(p.getIdProducto())))
                        .contentType(APPLICATION_JSON)
                        .body(BodyInserters.fromValue(p)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        var id = request.pathVariable("id");
        var productoDB = service.findById(id);

        return productoDB.flatMap(p -> service.delete(p)
                        .then(ServerResponse.accepted().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
