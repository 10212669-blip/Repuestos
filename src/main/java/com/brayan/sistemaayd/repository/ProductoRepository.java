// com.brayan.sistemaayd.repository/ProductoRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    Optional<Producto> findByCodigoProducto(String codigoProducto);
    
    List<Producto> findByEstado(String estado);
    
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.estado = 'Activo'")
    List<Producto> findProductosConStockBajo();
    
    @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria AND p.estado = 'Activo'")
    List<Producto> findByCategoria(@Param("idCategoria") Integer idCategoria);
    
    // ✅ BÚSQUEDA POR NOMBRE
    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "ORDER BY p.nombreProducto ASC")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);
}