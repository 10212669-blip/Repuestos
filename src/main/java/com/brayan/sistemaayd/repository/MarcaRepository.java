// com.lixy.sistemaayd.repository/MarcaRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Optional<Marca> findByNombreMarca(String nombreMarca);
    List<Marca> findByEstado(String estado);
}