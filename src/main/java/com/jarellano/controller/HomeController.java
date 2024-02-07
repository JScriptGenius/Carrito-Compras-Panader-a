package com.jarellano.controller;

import com.jarellano.entity.*;
import com.jarellano.repository.ProductoRepository;
import com.jarellano.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private DetalleOrdenService detalleOrdenService;

    /*PARA ALMACENAR LOS DETALLES DE LA ORDEN*/
    List<DetalleOrden> detalleOrdens = new ArrayList<DetalleOrden>();

    /*DATOS DE LA ORDEN*/
    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        model.addAttribute("productos",productoService.findAllProductos());
        /*SESSION*/
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id,Model model,HttpSession session) {
        Producto producto = productoService.findProductoById(id);
        model.addAttribute("producto",producto);
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        return "usuario/productohome";
    }

    @PostMapping("/car")
    public String addCar(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        double sumaTotal = 0;

        Producto producto = productoService.findProductoById(id);

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        /*VALIDAR QUE EL PRODUCTO NO SE AÑADA 2 VECES*/
        Integer id_producto = producto.getIdProducto();
        boolean ingresado = detalleOrdens.stream().anyMatch(p -> p.getProducto().getIdProducto() == id_producto);

        if (!ingresado) {
            detalleOrdens.add(detalleOrden);
        }

        sumaTotal=detalleOrdens.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);
        model.addAttribute("car",detalleOrdens);
        model.addAttribute("orden",orden);
        return "usuario/carrito";
    }

    @GetMapping("/delete/car/{id}")
    public String deleteProductoCar(@PathVariable Integer id, Model model) {
        /*LISTA NUEVA DE PRODUCTOS*/
        List<DetalleOrden> ordenesNueva = new ArrayList<>();
        for (DetalleOrden detalleOrden: detalleOrdens) {
            if(detalleOrden.getProducto().getIdProducto() != id){
                ordenesNueva.add(detalleOrden);
            }
        }
        /*PONER LA NUEVA LISTA CON LOS PRODUCTOS RESTANTES*/
        detalleOrdens = ordenesNueva;

        double sumaTotal = 0;
        sumaTotal=detalleOrdens.stream().mapToDouble(DetalleOrden::getTotal).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("car",detalleOrdens);
        model.addAttribute("orden",orden);
        return "usuario/carrito";
    }

    @GetMapping("/getCar")
    public String getCar(Model model, HttpSession session) {
        model.addAttribute("car",detalleOrdens);
        model.addAttribute("orden",orden);
        /*SESION*/
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {

        Usuario usuario = usuarioService.findUsuarioById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        model.addAttribute("car",detalleOrdens);
        model.addAttribute("orden",orden);
        model.addAttribute("usuario",usuario);

        return "usuario/resumenorden";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Date fechaCreacion = new Date();
        orden.setFecha_creacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());

        /*USUARIO*/
        Usuario usuario = usuarioService.findUsuarioById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        orden.setUsuario(usuario);
        ordenService.saveOrden(orden);

        /*GUARDAR DETALLES DE LA ORDEN*/
        for (DetalleOrden dt : detalleOrdens) {
            dt.setOrden(orden);
            detalleOrdenService.saveDetalleOrden(dt);
            /*ACTUALIZAR STOCK*/
            productoRepository.actualizarStock(dt.getProducto().getCantidad() - dt.getCantidad(),dt.getProducto().getIdProducto());
        }

        /*LIMPIAR LOS VALORES DE LA TABLA*/
        orden = new Orden();
        detalleOrdens.clear();
        redirectAttributes.addFlashAttribute("msgConfirmacionOrden","¡Orden registrada!");
        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model,  HttpSession session) {
        List<Producto> productos = productoService.findAllProductos().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
        model.addAttribute("productos",productos);
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        return "usuario/home";
    }
}
