// com.lixy.sistemaayd.repository/ProductoRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    List<Producto> findByCategoria(Integer idCategoria);
}
