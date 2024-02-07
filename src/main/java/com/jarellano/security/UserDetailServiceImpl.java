package com.jarellano.security;

import com.jarellano.entity.Usuario;
import com.jarellano.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optionalUsuario = usuarioService.findByEmail(username);
        if (optionalUsuario.isPresent()) {
            session.setAttribute("idusuario", optionalUsuario.get().getIdUsuario());
            Usuario usuario = optionalUsuario.get();
            return User.builder().username(usuario.getNombre()).password(usuario.getPassword()).roles(usuario.getTipo_usuario()).build();
        } else {
            throw new UsernameNotFoundException("¡Usuario no econtrado!");
        }
    }
}
