// com.lixy.sistemaayd/entity/Compra.java
package com.brayan.sistemaayd.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compras", schema = "tienda")
public class Compra implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;
    
    @Column(name = "codigo_compra", unique = true, nullable = false, length = 20)
    private String codigoCompra;
    
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "igv", precision = 10, scale = 2)
    private BigDecimal igv;
    
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado_compra", length = 20)
    private String estadoCompra;
    
    @Column(name = "tipo_comprobante", length = 20)
    private String tipoComprobante;
    
    @Column(name = "numero_comprobante", length = 50)
    private String numeroComprobante;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles;
    
    // Constructores
    public Compra() {
        this.subtotal = BigDecimal.ZERO;
        this.igv = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.estadoCompra = "Pendiente";
        this.fechaCompra = LocalDateTime.now();
    }
    
    // ===== GETTERS Y SETTERS (UNO DE CADA UNO) =====
    public Integer getIdCompra() { 
        return idCompra; 
    }
    
    public void setIdCompra(Integer idCompra) { 
        this.idCompra = idCompra; 
    }
    
    public String getCodigoCompra() { 
        return codigoCompra; 
    }
    
    public void setCodigoCompra(String codigoCompra) { 
        this.codigoCompra = codigoCompra; 
    }
    
    public Proveedor getProveedor() { 
        return proveedor; 
    }
    
    public void setProveedor(Proveedor proveedor) { 
        this.proveedor = proveedor; 
    }
    
    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }
    
    public LocalDateTime getFechaCompra() { 
        return fechaCompra; 
    }
    
    public void setFechaCompra(LocalDateTime fechaCompra) { 
        this.fechaCompra = fechaCompra; 
    }
    
    public BigDecimal getSubtotal() { 
        return subtotal; 
    }
    
    public void setSubtotal(BigDecimal subtotal) { 
        this.subtotal = subtotal; 
    }
    
    public BigDecimal getIgv() { 
        return igv; 
    }
    
    public void setIgv(BigDecimal igv) { 
        this.igv = igv; 
    }
    
    public BigDecimal getTotal() { 
        return total; 
    }
    
    public void setTotal(BigDecimal total) { 
        this.total = total; 
    }
    
    public String getEstadoCompra() { 
        return estadoCompra; 
    }
    
    public void setEstadoCompra(String estadoCompra) { 
        this.estadoCompra = estadoCompra; 
    }
    
    public String getTipoComprobante() { 
        return tipoComprobante; 
    }
    
    public void setTipoComprobante(String tipoComprobante) { 
        this.tipoComprobante = tipoComprobante; 
    }
    
    public String getNumeroComprobante() { 
        return numeroComprobante; 
    }
    
    public void setNumeroComprobante(String numeroComprobante) { 
        this.numeroComprobante = numeroComprobante; 
    }
    
    public String getObservaciones() { 
        return observaciones; 
    }
    
    public void setObservaciones(String observaciones) { 
        this.observaciones = observaciones; 
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
    
    public List<DetalleCompra> getDetalles() { 
        return detalles; 
    }
    
    public void setDetalles(List<DetalleCompra> detalles) { 
        this.detalles = detalles; 
    }
}