// com.lixy.sistemaayd.repository/DetalleVentaRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    
    List<DetalleVenta> findByVentaIdVenta(Integer idVenta);
    
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.producto.idProducto = :idProducto")
    List<DetalleVenta> findByProducto(@Param("idProducto") Integer idProducto);
    
    @Query("SELECT dv.producto.idProducto, SUM(dv.cantidad) as totalVendido " +
           "FROM DetalleVenta dv " +
           "WHERE dv.venta.estadoVenta = 'Completado' " +
           "GROUP BY dv.producto.idProducto " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();
}
