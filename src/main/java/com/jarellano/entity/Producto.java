package com.jarellano.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private String imagen;
    private double precio;
    private int cantidad;

    @ManyToOne
    private Usuario usuario;
}