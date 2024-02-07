package com.jarellano.entity;

import javax.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;
    private String numero;
    private Date fecha_creacion;
    private Date fecha_recibida;
    private double total;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(
            mappedBy = "orden"
    )
    private List<DetalleOrden> detalleOrden;
}