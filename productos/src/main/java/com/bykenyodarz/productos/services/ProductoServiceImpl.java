package com.bykenyodarz.productos.services;

import com.bykenyodarz.productos.models.Producto;
import com.bykenyodarz.productos.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductService {

    private final ProductoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Flux<Producto> findAllProductos() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Producto> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return repository.delete(producto);
    }
}
