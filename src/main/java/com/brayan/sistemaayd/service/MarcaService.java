// com.lixy.sistemaayd.service/MarcaService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Marca;
import com.brayan.sistemaayd.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    public List<Marca> listarTodos() {
        return marcaRepository.findAll();
    }
    
    public List<Marca> listarActivos() {
        return marcaRepository.findByEstado("Activo");
    }
    
    public Optional<Marca> buscarPorId(Integer id) {
        return marcaRepository.findById(id);
    }
    
    public Marca guardar(Marca marca) {
        return marcaRepository.save(marca);
    }
}