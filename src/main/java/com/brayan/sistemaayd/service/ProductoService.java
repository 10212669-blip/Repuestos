// com.lixy.sistemaayd.service/ProductoService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    
    public List<Producto> listarActivos() {
        return productoRepository.findByEstado("Activo");
    }
    
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }
    
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo);
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }
    
    public List<Producto> productosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }
    
    @Transactional
    public Producto guardar(Producto producto) {
        if (producto.getIdProducto() == null) {
            producto.setFechaRegistro(LocalDateTime.now());
        }
        producto.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
    
    @Transactional
    public void desactivar(Integer id) {
        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isPresent()) {
            Producto producto = opt.get();
            producto.setEstado("Inactivo");
            producto.setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto);
        }
    }
}
