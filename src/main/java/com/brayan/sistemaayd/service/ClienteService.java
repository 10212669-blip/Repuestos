// com.lixy.sistemaayd.service/ClienteService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Cliente;
import com.brayan.sistemaayd.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado("Activo");
    }
    
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorCodigo(String codigo) {
        return clienteRepository.findByCodigoCliente(codigo);
    }
    
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }
    
    @Transactional
    public Cliente guardar(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            // Generar código automático si no tiene
            if (cliente.getCodigoCliente() == null || cliente.getCodigoCliente().isEmpty()) {
                long count = clienteRepository.count() + 1;
                cliente.setCodigoCliente(String.format("CLI%04d", count));
            }
            cliente.setFechaRegistro(LocalDateTime.now());
        }
        cliente.setFechaActualizacion(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }
    
    @Transactional
    public void desactivar(Integer id) {
        Optional<Cliente> opt = clienteRepository.findById(id);
        if (opt.isPresent()) {
            Cliente cliente = opt.get();
            cliente.setEstado("Inactivo");
            cliente.setFechaActualizacion(LocalDateTime.now());
            clienteRepository.save(cliente);
        }
    }
}