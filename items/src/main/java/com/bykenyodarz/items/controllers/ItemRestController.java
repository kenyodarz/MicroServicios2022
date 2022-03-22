package com.bykenyodarz.items.controllers;

import com.bykenyodarz.items.models.Item;
import com.bykenyodarz.items.models.Producto;
import com.bykenyodarz.items.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RefreshScope
@EnableAutoConfiguration
@RestController
public class ItemRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRestController.class);

    private final ItemService itemService;

    private final Environment env;

    private final CircuitBreakerFactory cbFactory;

    @Value("${configuracion.texto}")
    private String text;

    @GetMapping("/all")
    public List<Item> listar() {
        return itemService.findAll();
    }

    @GetMapping("/{id}/{cantidad}")
    public Item getItem(@PathVariable String id, @PathVariable Integer cantidad) {
        return cbFactory.create("items")
                .run(() -> itemService.findById(id, cantidad)
                        , throwable ->
                                metodoAlternativo(id, cantidad, throwable)
                );
    }

    @GetMapping("/v2/{id}/{cantidad}")
    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    public Item getItem2(@PathVariable String id, @PathVariable Integer cantidad) {
        return itemService.findById(id, cantidad);
    }


    @GetMapping("/v3/{id}/{cantidad}")
    @TimeLimiter(name = "items", fallbackMethod = "metodoAlternativo2")
    public CompletableFuture<Item> getItem3(@PathVariable String id, @PathVariable Integer cantidad) {
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
    }

    public Item metodoAlternativo(String id, Integer cantidad, Throwable ex) {
        var item = new Item();
        var product = new Producto();

        item.setCantidad(cantidad);

        product.setIdProducto(id);
        product.setNombre("Producto Prueba");
        product.setPrecio(500.00);
        product.setCreatedAt(LocalDateTime.now());

        item.setProducto(product);

        LOGGER.info("Response 200, fallback method for error: {}", ex.getMessage());

        return item;
    }

    public CompletableFuture<Item> metodoAlternativo2(String id, Integer cantidad, Throwable ex) {
        var item = new Item();
        var product = new Producto();

        item.setCantidad(cantidad);

        product.setIdProducto(id);
        product.setNombre("Producto Prueba");
        product.setPrecio(500.00);
        product.setCreatedAt(LocalDateTime.now());

        item.setProducto(product);

        LOGGER.info("Response 200, fallback method for error: {}", ex.getMessage());

        return CompletableFuture.supplyAsync(() -> item);
    }

    @GetMapping("/obtener-config")
    public ResponseEntity<Map<String, String>> getConfig(@Value("${server.port}") String puerto) {
        LOGGER.info("Configuración -> {}", text);
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("text", text);
        jsonResponse.put("puerto", puerto);
        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            jsonResponse.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            jsonResponse.put("autor.email", env.getProperty("configuracion.autor.email"));
        }
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        var productoResponse = itemService.save(producto);
        return ResponseEntity.created(URI
                        .create("/".concat(productoResponse.getIdProducto())))
                .body(producto);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        itemService.delete(id);
    }
}
