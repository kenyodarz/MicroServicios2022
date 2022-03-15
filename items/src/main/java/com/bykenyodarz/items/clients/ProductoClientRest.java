package com.bykenyodarz.items.clients;

import com.bykenyodarz.items.models.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "servicio-productos")
public interface ProductoClientRest {

    @GetMapping("/api/v1/productos")
    List<Producto> listar();

    @GetMapping("/api/v1/productos/{id}")
    Producto getOne(@PathVariable String id);

    @PostMapping("/api/v1/productos")
    Producto create(@RequestBody Producto producto);

    @DeleteMapping("/api/v1/productos/{id}")
    void eliminar(@PathVariable String id);

}
