// com.lixy.sistemaayd.repository/ProveedorRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    Optional<Proveedor> findByCodigoProveedor(String codigoProveedor);
    List<Proveedor> findByEstado(String estado);
    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombre);
}
