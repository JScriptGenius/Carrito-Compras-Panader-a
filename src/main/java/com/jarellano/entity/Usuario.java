package com.jarellano.entity;

import javax.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    private String nombre;
    private String username;
    private String email;
    private String direccion;
    private String telefono;
    private String tipo_usuario;
    private String password;

    @OneToMany(
            mappedBy = "usuario"
    )
    private List<Producto> productos;

    @OneToMany(
            mappedBy = "usuario"
    )
    private List<Orden> ordenes;
}