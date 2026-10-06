// com.lixy.sistemaayd/repository/DetalleCompraRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
    
    List<DetalleCompra> findByCompraIdCompra(Integer idCompra);
    
    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.producto.idProducto = :idProducto")
    List<DetalleCompra> findByProducto(@Param("idProducto") Integer idProducto);
}