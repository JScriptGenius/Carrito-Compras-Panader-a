package com.jarellano.controller;

import com.jarellano.entity.*;
import com.jarellano.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /* /usuario/registro */
    @GetMapping("/registro")
    public String Create(){
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario, RedirectAttributes redirectAttributes) {
        usuario.setTipo_usuario("USER");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.saveUsuario(usuario);
        redirectAttributes.addFlashAttribute("msgConfirmacionUser","¡Usuario registrado correctamente!");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @GetMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, RedirectAttributes redirectAttributes){
        Optional<Usuario> user = usuarioService.findUsuarioById(Integer.parseInt(session.getAttribute("idusuario").toString()));
        if (user.isPresent()) {
            session.setAttribute("idusuario",user.get().getIdUsuario());
            if (user.get().getTipo_usuario().equals("ADMIN")) {
                return "redirect:/administrador";
            } else {
                return "redirect:/";
            }
        } else {

        }
        redirectAttributes.addFlashAttribute("msgInvalidUser","¡Usuario inválido!");
        return "redirect:/usuario/login";
    }

    @GetMapping("/compras")
    public String obtenerCompras(HttpSession session, Model model) {
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        Usuario usuario = usuarioService.findUsuarioById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordens = ordenService.findByUsuario(usuario);
        model.addAttribute("ordens",ordens);
        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model){
        Orden orden = ordenService.findOrdenById(id);
        model.addAttribute("detalles",orden.getDetalleOrden());
        /*SESSION*/
        model.addAttribute("sesion",session.getAttribute("idusuario"));
        return "usuario/detallecompra";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session){
        session.removeAttribute("idusuario");
        return "redirect:/";
    }
}
