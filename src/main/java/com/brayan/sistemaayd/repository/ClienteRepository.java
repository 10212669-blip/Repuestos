// com.lixy.sistemaayd.repository/ClienteRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCodigoCliente(String codigoCliente);
    List<Cliente> findByEstado(String estado);
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);
}
