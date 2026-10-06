// com.lixy.sistemaayd.service/ProveedorService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Proveedor;
import com.brayan.sistemaayd.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }
    
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEstado("Activo");
    }
    
    public Optional<Proveedor> buscarPorId(Integer id) {
        return proveedorRepository.findById(id);
    }
    
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void desactivar(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}