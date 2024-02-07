package com.jarellano.service;

import com.jarellano.entity.*;
import com.jarellano.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrdenServiceImpl implements OrdenService{

    @Autowired
    OrdenRepository ordenRepository;

    @Override
    public List<Orden> findAllOrdenes() {
        return ordenRepository.findAll();
    }

    @Override
    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    /*00000010*/
    @Override
    public String generarNumeroOrden(){
        int numero = 0;
        String numeroConcatenado="";
        List<Orden> ordenes = findAllOrdenes();
        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

        if (ordenes.isEmpty()) {
            numero = 1;
        }else {
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        if (numero < 10) {
            numeroConcatenado = "000000000"+String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000"+String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "0000000"+String.valueOf(numero);
        } else if (numero < 10000) {
            numeroConcatenado = "000000"+String.valueOf(numero);
        }
        return numeroConcatenado;
    }

    @Override
    public Orden findOrdenById(Integer id) {
        Orden orden = ordenRepository.findById(id).get();
        return orden;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }
}
