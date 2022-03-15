package com.bykenyodarz.items.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Producto {

    private String idProducto;
    @NotEmpty
    private String nombre;
    @NotNull
    private Double precio;
    private LocalDateTime createdAt;

}
