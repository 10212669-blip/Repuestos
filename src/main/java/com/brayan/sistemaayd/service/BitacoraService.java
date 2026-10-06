// com.lixy.sistemaayd/service/BitacoraService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.LogUsuario;
import com.brayan.sistemaayd.repository.LogUsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BitacoraService {
    
    @Autowired
    private LogUsuarioRepository logUsuarioRepository;
    
    @Transactional
    public void registrar(Integer idUsuario, String accion, String detalle, HttpServletRequest request) {
        try {
            LogUsuario log = new LogUsuario();
            log.setIdUsuario(idUsuario);
            log.setAccion(accion);
            log.setDetalle(detalle);
            log.setFechaHora(LocalDateTime.now());
            
            if (request != null) {
                log.setIpOrigen(obtenerIpCliente(request));
                log.setUserAgent(request.getHeader("User-Agent"));
            }
            
            logUsuarioRepository.save(log);
            System.out.println("📝 Bitácora: " + accion + " - " + detalle);
            
        } catch (Exception e) {
            System.err.println("Error al registrar en bitácora: " + e.getMessage());
        }
    }
    
    private String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public List<LogUsuario> listarTodos() {
        return logUsuarioRepository.findAll();
    }
    
    public List<LogUsuario> listarUltimos() {
        return logUsuarioRepository.findTop50ByOrderByFechaHoraDesc();
    }
    
    public List<LogUsuario> buscarPorUsuario(Integer idUsuario) {
        return logUsuarioRepository.findByIdUsuarioOrderByFechaHoraDesc(idUsuario);
    }
    
    public List<LogUsuario> buscarPorAccion(String accion) {
        return logUsuarioRepository.findByAccionOrderByFechaHoraDesc(accion);
    }
    
    public List<LogUsuario> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return logUsuarioRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);
    }
}