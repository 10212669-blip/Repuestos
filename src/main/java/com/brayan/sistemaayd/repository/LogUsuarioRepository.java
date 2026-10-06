// com.lixy.sistemaayd/repository/LogUsuarioRepository.java
package com.brayan.sistemaayd.repository;

import com.brayan.sistemaayd.entity.LogUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogUsuarioRepository extends JpaRepository<LogUsuario, Integer> {
    
    List<LogUsuario> findByIdUsuarioOrderByFechaHoraDesc(Integer idUsuario);
    
    List<LogUsuario> findByAccionOrderByFechaHoraDesc(String accion);
    
    List<LogUsuario> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime inicio, LocalDateTime fin);
    
    List<LogUsuario> findTop50ByOrderByFechaHoraDesc();
}