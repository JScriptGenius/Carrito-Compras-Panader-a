package com.jarellano.service;

import com.jarellano.entity.*;
import java.util.List;

public interface OrdenService {

    List<Orden> findAllOrdenes();
    Orden saveOrden(Orden orden);
    String generarNumeroOrden();
    Orden findOrdenById(Integer id);
    List<Orden> findByUsuario (Usuario usuario);
}
