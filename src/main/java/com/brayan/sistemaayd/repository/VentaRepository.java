// com.lixy.sistemaayd/repository/VentaRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    Optional<Venta> findByCodigoVenta(String codigoVenta);
    
    List<Venta> findByEstadoVenta(String estado);
    
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT v FROM Venta v WHERE v.cliente.idCliente = :idCliente ORDER BY v.fechaVenta DESC")
    List<Venta> findVentasByCliente(@Param("idCliente") Integer idCliente);
    
    @Query("SELECT v FROM Venta v WHERE v.usuario.idUsuario = :idUsuario ORDER BY v.fechaVenta DESC")
    List<Venta> findVentasByUsuario(@Param("idUsuario") Integer idUsuario);
    
    @Query(value = "SELECT COALESCE(SUM(v.total), 0) FROM tienda.ventas v WHERE v.estado_venta = 'Completado' AND DATE(v.fecha_venta) = CURRENT_DATE", nativeQuery = true)
    BigDecimal sumTotalVentasHoy();
    
    @Query(value = "SELECT COALESCE(COUNT(v), 0) FROM tienda.ventas v WHERE v.estado_venta = 'Completado' AND DATE(v.fecha_venta) = CURRENT_DATE", nativeQuery = true)
    Long countVentasHoy();
    
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.estadoVenta = 'Completado' AND v.fechaVenta BETWEEN :inicio AND :fin")
    BigDecimal sumTotalVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT COALESCE(COUNT(v), 0) FROM Venta v WHERE v.estadoVenta = 'Completado' AND v.fechaVenta BETWEEN :inicio AND :fin")
    Long countVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT FUNCTION('DATE', v.fechaVenta) as dia, COALESCE(SUM(v.total), 0) as total " +
           "FROM Venta v WHERE v.estadoVenta = 'Completado' AND v.fechaVenta >= :fechaInicio " +
           "GROUP BY FUNCTION('DATE', v.fechaVenta) ORDER BY dia ASC")
    List<Object[]> findVentasAgrupadasPorDia(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    @Query(value = "SELECT COALESCE(AVG(v.total), 0) FROM tienda.ventas v WHERE v.estado_venta = 'Completado' AND DATE(v.fecha_venta) = CURRENT_DATE", nativeQuery = true)
    BigDecimal avgTotalVentasHoy();
}