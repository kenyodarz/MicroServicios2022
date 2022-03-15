package com.bykenyodarz.productos.repositories;


import com.bykenyodarz.productos.models.Producto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, String> {



}
