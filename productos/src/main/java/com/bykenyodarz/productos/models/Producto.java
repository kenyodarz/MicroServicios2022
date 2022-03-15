package com.bykenyodarz.productos.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table("productos")
public class Producto {

    @Id
    private String idProducto;
    @Column
    @NotEmpty
    private String nombre;
    @Column
    @NotNull
    private Double precio;
    @Column
    private LocalDateTime createdAt;

}
