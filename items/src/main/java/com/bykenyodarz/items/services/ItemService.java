package com.bykenyodarz.items.services;

import com.bykenyodarz.items.models.Item;
import com.bykenyodarz.items.models.Producto;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    Item findById(String id, Integer cantidad);

    Producto save(Producto producto);

    void delete(String id);
}
