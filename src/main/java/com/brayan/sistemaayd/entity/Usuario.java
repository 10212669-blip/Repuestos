// com.lixy.sistemaayd.entity/Usuario.java
package com.brayan.sistemaayd.entity;

import jakarta.persistence.*;  // ← IMPORTANTE: usar jakarta, no javax
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", schema = "tienda")
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    @Column(name = "usuario", unique = true, nullable = false, length = 50)
    private String usuario;
    
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;
    
    @Column(name = "rol", nullable = false, length = 20)
    private String rol;
    
    @Column(name = "estado", length = 15)
    private String estado;
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // CONSTRUCTORES
    public Usuario() {
    }
    
    public Usuario(String usuario, String contrasena, String email, String nombreCompleto, String rol) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.estado = "Activo";
    }
    
    // GETTERS Y SETTERS
    public Integer getIdUsuario() { 
        return idUsuario; 
    }
    
    public void setIdUsuario(Integer idUsuario) { 
        this.idUsuario = idUsuario; 
    }
    
    public String getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(String usuario) { 
        this.usuario = usuario; 
    }
    
    public String getContrasena() { 
        return contrasena; 
    }
    
    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getNombreCompleto() { 
        return nombreCompleto; 
    }
    
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }
    
    public String getRol() { 
        return rol; 
    }
    
    public void setRol(String rol) { 
        this.rol = rol; 
    }
    
    public String getEstado() { 
        return estado; 
    }
    
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
    
    public LocalDateTime getUltimoAcceso() { 
        return ultimoAcceso; 
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { 
        this.ultimoAcceso = ultimoAcceso; 
    }
    
    public LocalDateTime getFechaCreacion() { 
        return fechaCreacion; 
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    public LocalDateTime getFechaActualizacion() { 
        return fechaActualizacion; 
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { 
        this.fechaActualizacion = fechaActualizacion; 
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", usuario='" + usuario + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}