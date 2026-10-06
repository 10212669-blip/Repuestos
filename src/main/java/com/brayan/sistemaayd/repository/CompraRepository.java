// com.lixy.sistemaayd/repository/CompraRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    
    Optional<Compra> findByCodigoCompra(String codigoCompra);
    
    List<Compra> findByEstadoCompra(String estado);
    
    List<Compra> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT c FROM Compra c WHERE c.proveedor.idProveedor = :idProveedor ORDER BY c.fechaCompra DESC")
    List<Compra> findComprasByProveedor(@Param("idProveedor") Integer idProveedor);
    
    @Query("SELECT c FROM Compra c WHERE c.usuario.idUsuario = :idUsuario ORDER BY c.fechaCompra DESC")
    List<Compra> findComprasByUsuario(@Param("idUsuario") Integer idUsuario);
}
