package com.jarellano.controller;

import com.jarellano.entity.*;
import com.jarellano.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos",productoService.findAllProductos());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img")MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        Usuario usuario = usuarioService.findUsuarioById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(usuario);
        /*IMG*/
        if (producto.getIdProducto() == null) { //Cuando se crea un producto
            String nombreImagen= uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {}
        productoService.saveProducto(producto);
        redirectAttributes.addFlashAttribute("msgConfirmacion","¡Producto registrado correctamente!");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Producto producto = productoService.findProductoById(id);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Integer id, Producto producto, @RequestParam("img") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException{
        Producto productoDB = productoService.findProductoById(id);
        producto.setIdProducto(productoDB.getIdProducto());
        productoDB.setNombre(producto.getNombre());
        productoDB.setDescripcion(producto.getDescripcion());
        if (file.isEmpty()) { //Caundo editamos el producto pero no cambiamos la imagen
            producto.setImagen(productoDB.getImagen());
        } else { //Cuando se edita tambien la imagen
            /*Para eliminar cuando no sea la imagen por defecto*/
            if (!productoDB.getImagen().equals("default.jpg")) {
                uploadFileService.deleteImage(productoDB.getImagen());
            }
            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        productoDB.setPrecio(producto.getPrecio());
        productoDB.setCantidad(producto.getCantidad());
        producto.setUsuario(productoDB.getUsuario());
        productoService.updateProducto(producto);
        redirectAttributes.addFlashAttribute("msgConfirmacion","¡Producto actualizado correctamente!");
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmpleado(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Producto productoDB = productoService.findProductoById(id);
        /*Para eliminar cuando no sea la imagen por defecto*/
        if (!productoDB.getImagen().equals("default.jpg")) {
            uploadFileService.deleteImage(productoDB.getImagen());
        }
        productoService.deleteProducto(id);
        redirectAttributes.addFlashAttribute("msgConfirmacion","¡Producto eliminado correctamente!");
        return "redirect:/productos";
    }
}
