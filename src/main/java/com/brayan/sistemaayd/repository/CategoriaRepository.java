// com.lixy.sistemaayd.repository/CategoriaRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombreCategoria(String nombreCategoria);
    List<Categoria> findByEstado(String estado);
}
