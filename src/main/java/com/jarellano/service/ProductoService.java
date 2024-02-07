package com.jarellano.service;

import com.jarellano.entity.Producto;
import java.util.List;

public interface ProductoService {

    public List<Producto> findAllProductos();
    public Producto saveProducto(Producto producto);
    public Producto findProductoById(Integer id);
    public void updateProducto(Producto producto);
    public void deleteProducto(Integer id);
}
