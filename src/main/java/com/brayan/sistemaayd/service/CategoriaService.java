// com.lixy.sistemaayd.service/CategoriaService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Categoria;
import com.brayan.sistemaayd.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }
    
    public List<Categoria> listarActivos() {
        return categoriaRepository.findByEstado("Activo");
    }
    
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }
    
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
}
