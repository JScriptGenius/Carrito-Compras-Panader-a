package com.jarellano.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detalle_ordenes")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle_orden;
    private String nombre;
    private int cantidad;
    private double precio;
    private double total;

    @ManyToOne
    private Orden orden;

    @ManyToOne
    private Producto producto;
}