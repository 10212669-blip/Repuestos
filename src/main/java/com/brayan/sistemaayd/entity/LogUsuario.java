// com.lixy.sistemaayd/entity/LogUsuario.java
package com.brayan.sistemaayd.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_usuarios", schema = "auditoria")
public class LogUsuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;
    
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    @Column(name = "accion", nullable = false, length = 50)
    private String accion;
    
    @Column(name = "detalle")
    private String detalle;
    
    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
    
    // Constructores
    public LogUsuario() {
        this.fechaHora = LocalDateTime.now();
    }
    
    public LogUsuario(Integer idUsuario, String accion, String detalle) {
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.detalle = detalle;
        this.fechaHora = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getIdLog() { return idLog; }
    public void setIdLog(Integer idLog) { this.idLog = idLog; }
    
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    
    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}