package com.jarellano.service;

import com.jarellano.repository.ProductoRepository;
import com.jarellano.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<Producto> findAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto findProductoById(Integer id) {
        Producto producto = productoRepository.findById(id).get();
        return producto;
    }

    @Override
    public void updateProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }
}
