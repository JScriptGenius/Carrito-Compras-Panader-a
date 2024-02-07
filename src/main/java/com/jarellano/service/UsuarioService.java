package com.jarellano.service;

import com.jarellano.entity.Usuario;
import java.util.*;

public interface UsuarioService {
    public List<Usuario> findAllUsuarios();
    public Usuario saveUsuario(Usuario usuario);
    public Optional<Usuario> findUsuarioById(Integer id);
    public Optional<Usuario> findByEmail(String email);
}
