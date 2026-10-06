// com.lixy.sistemaayd/service/CompraService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.DetalleCompra;
import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.entity.Compra;
import com.brayan.sistemaayd.repository.DetalleCompraRepository;
import com.brayan.sistemaayd.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {
    
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
    @Autowired
    private ProductoService productoService;
    
    // ===== LISTAR =====
    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }
    
    public List<Compra> listarActivas() {
        return compraRepository.findByEstadoCompra("Recibido");
    }
    
    public List<Compra> listarPorProveedor(Integer idProveedor) {
        return compraRepository.findComprasByProveedor(idProveedor);
    }
    
    // ===== BUSCAR =====
    public Optional<Compra> buscarPorId(Integer id) {
        return compraRepository.findById(id);
    }
    
    public Optional<Compra> buscarPorCodigo(String codigo) {
        return compraRepository.findByCodigoCompra(codigo);
    }
    
    // ===== GUARDAR =====
    @Transactional
    public Compra guardar(Compra compra) {
        if (compra.getIdCompra() == null) {
            long count = compraRepository.count() + 1;
            compra.setCodigoCompra(String.format("C%04d", count));
            compra.setFechaCreacion(LocalDateTime.now());
            compra.setFechaCompra(LocalDateTime.now());
        }
        compra.setFechaActualizacion(LocalDateTime.now());
        calcularTotales(compra);
        return compraRepository.save(compra);
    }
    
    // ===== REGISTRAR COMPRA CON DETALLES =====
    @Transactional
    public Compra registrarCompra(Compra compra, List<DetalleCompra> detalles) {
        compra.setDetalles(detalles);
        
        Compra compraGuardada = guardar(compra);
        
        // Actualizar stock
        for (DetalleCompra detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            producto.setPrecioCompra(detalle.getPrecioUnitario());
            productoService.guardar(producto);
        }
        
        // Cambiar estado a Recibido
        compraGuardada.setEstadoCompra("Recibido");
        compraRepository.save(compraGuardada);
        
        return compraGuardada;
    }
    
    // ===== ANULAR =====
    @Transactional
    public void anularCompra(Integer id) {
        Optional<Compra> opt = compraRepository.findById(id);
        if (opt.isPresent()) {
            Compra compra = opt.get();
            compra.setEstadoCompra("Cancelado");
            compra.setFechaActualizacion(LocalDateTime.now());
            compraRepository.save(compra);
        }
    }
    
    // ===== CALCULAR TOTALES =====
    private void calcularTotales(Compra compra) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        if (compra.getDetalles() != null) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                detalle.setCompra(compra);
                
                BigDecimal precio = detalle.getPrecioUnitario();
                BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
                
                BigDecimal subtotalDetalle = precio.multiply(cantidad);
                BigDecimal igvDetalle = subtotalDetalle.multiply(IGV_PORCENTAJE)
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalDetalle = subtotalDetalle.add(igvDetalle)
                        .setScale(2, RoundingMode.HALF_UP);
                
                detalle.setSubtotal(subtotalDetalle.setScale(2, RoundingMode.HALF_UP));
                detalle.setIgv(igvDetalle);
                detalle.setTotal(totalDetalle);
                
                subtotal = subtotal.add(subtotalDetalle);
            }
        }
        
        BigDecimal igvTotal = subtotal.multiply(IGV_PORCENTAJE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igvTotal)
                .setScale(2, RoundingMode.HALF_UP);
        
        compra.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        compra.setIgv(igvTotal);
        compra.setTotal(total);
    }
}