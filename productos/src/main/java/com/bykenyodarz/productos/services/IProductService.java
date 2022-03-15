package com.bykenyodarz.productos.services;

import com.bykenyodarz.productos.models.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {

    Flux<Producto> findAllProductos();

    Mono<Producto> findById(String id);

    Mono<Producto> save(Producto producto);

    Mono<Void> delete(Producto producto);

}
