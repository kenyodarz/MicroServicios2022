package com.bykenyodarz.items.services;

import com.bykenyodarz.items.clients.ProductoClientRest;
import com.bykenyodarz.items.models.Item;
import com.bykenyodarz.items.models.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceFeign implements ItemService {

    private final ProductoClientRest clientRest;

    @Override
    public List<Item> findAll() {
        return clientRest.listar().stream().map(producto -> new Item(producto, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(String id, Integer cantidad) {
        return new Item(clientRest.getOne(id), cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return clientRest.create(producto);
    }

    @Override
    public void delete(String id) {
        clientRest.eliminar(id);
    }
}
