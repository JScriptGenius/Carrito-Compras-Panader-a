package com.jarellano.controller;

import com.jarellano.entity.*;
import com.jarellano.service.*;
import com.jarellano.service.UsuarioService;
import com.jarellano.entity.Orden;
import com.jarellano.entity.Producto;
import com.jarellano.service.OrdenService;
import com.jarellano.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @GetMapping("")
    public String home(Model model) {
        List<Producto> productos = productoService.findAllProductos();
        model.addAttribute("productos",productos);
        return "administrador/home";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model){
        model.addAttribute("usuarios",usuarioService.findAllUsuarios());
        return "administrador/usuarios";
    }

    @GetMapping("/ordenes")
    public String ordenes(Model model){
        model.addAttribute("ordenes",ordenService.findAllOrdenes());
        return "administrador/ordenes";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model){
        Orden orden = ordenService.findOrdenById(id);
        model.addAttribute("detalles",orden.getDetalleOrden());
        return "administrador/detalleorden";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model) {
        List<Producto> productos = productoService.findAllProductos().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
        model.addAttribute("productos",productos);
        return "administrador/home";
    }
}