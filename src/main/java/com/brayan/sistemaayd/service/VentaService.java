// com.lixy.sistemaayd/service/VentaService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.DetalleVenta;
import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.entity.Venta;
import com.brayan.sistemaayd.repository.DetalleVentaRepository;
import com.brayan.sistemaayd.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
    
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ProductoService productoService;
    
    // ============================================
    // LISTAR
    // ============================================
    public List<Venta> listarTodas() {
        try {
            List<Venta> ventas = ventaRepository.findAll();
            System.out.println("📋 listarTodas() - Encontradas: " + ventas.size());
            return ventas;
        } catch (Exception e) {
            System.err.println("🔥 ERROR en listarTodas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Venta> listarActivas() {
        return ventaRepository.findByEstadoVenta("Completado");
    }
    
    public List<Venta> listarPorCliente(Integer idCliente) {
        return ventaRepository.findVentasByCliente(idCliente);
    }
    
    public List<Venta> listarPorUsuario(Integer idUsuario) {
        return ventaRepository.findVentasByUsuario(idUsuario);
    }
    
    public long contarTodas() {
        try {
            return ventaRepository.count();
        } catch (Exception e) {
            System.err.println("🔥 ERROR en contarTodas: " + e.getMessage());
            return 0;
        }
    }
    
    // ============================================
    // BUSCAR
    // ============================================
    public Optional<Venta> buscarPorId(Integer id) {
        try {
            System.out.println("🔍 VentaService - Buscando ID: " + id);
            Optional<Venta> venta = ventaRepository.findById(id);
            if (venta.isPresent()) {
                System.out.println("✅ VentaService - Encontrada: " + venta.get().getCodigoVenta());
            } else {
                System.out.println("❌ VentaService - NO encontrada ID: " + id);
                System.out.println("📋 Total en BD: " + ventaRepository.count());
            }
            return venta;
        } catch (Exception e) {
            System.err.println("🔥 ERROR en buscarPorId: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    public Optional<Venta> buscarPorCodigo(String codigo) {
        return ventaRepository.findByCodigoVenta(codigo);
    }
    
    // ============================================
    // ESTADÍSTICAS
    // ============================================
    public BigDecimal getTotalVentasHoy() {
        try {
            return ventaRepository.sumTotalVentasHoy();
        } catch (Exception e) {
            System.err.println("Error al obtener total ventas hoy: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    public Long getCantidadVentasHoy() {
        try {
            return ventaRepository.countVentasHoy();
        } catch (Exception e) {
            System.err.println("Error al obtener cantidad ventas hoy: " + e.getMessage());
            return 0L;
        }
    }
    
    public BigDecimal getTotalVentasMes() {
        try {
            YearMonth mesActual = YearMonth.now();
            LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
            LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);
            return ventaRepository.sumTotalVentasEntreFechas(inicioMes, finMes);
        } catch (Exception e) {
            System.err.println("Error al obtener total ventas del mes: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    public Long getCantidadVentasMes() {
        try {
            YearMonth mesActual = YearMonth.now();
            LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
            LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);
            return ventaRepository.countVentasEntreFechas(inicioMes, finMes);
        } catch (Exception e) {
            System.err.println("Error al obtener cantidad ventas del mes: " + e.getMessage());
            return 0L;
        }
    }
    
    public List<Venta> getVentasDelMes() {
        YearMonth mesActual = YearMonth.now();
        LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);
        return ventaRepository.findByFechaVentaBetween(inicioMes, finMes);
    }
    
    public List<Object[]> getVentasUltimos7Dias() {
        try {
            LocalDateTime fechaInicio = LocalDateTime.now().minusDays(7);
            return ventaRepository.findVentasAgrupadasPorDia(fechaInicio);
        } catch (Exception e) {
            System.err.println("Error al obtener ventas de los últimos 7 días: " + e.getMessage());
            return List.of();
        }
    }
    
    public BigDecimal getTotalVentasPorDia(LocalDateTime fecha) {
        try {
            LocalDateTime inicio = fecha.toLocalDate().atStartOfDay();
            LocalDateTime fin = fecha.toLocalDate().atTime(23, 59, 59);
            return ventaRepository.sumTotalVentasEntreFechas(inicio, fin);
        } catch (Exception e) {
            System.err.println("Error al obtener total ventas por día: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    // ============================================
    // GUARDAR
    // ============================================
    @Transactional
    public Venta guardar(Venta venta) {
        if (venta.getIdVenta() == null) {
            long count = ventaRepository.count() + 1;
            venta.setCodigoVenta(String.format("V%04d", count));
            venta.setFechaCreacion(LocalDateTime.now());
            venta.setFechaVenta(LocalDateTime.now());
        }
        venta.setFechaActualizacion(LocalDateTime.now());
        calcularTotales(venta);
        return ventaRepository.save(venta);
    }
    
    // ============================================
    // REGISTRAR VENTA CON DETALLES
    // ============================================
    @Transactional
    public Venta registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        venta.setDetalles(detalles);
        
        // Verificar stock antes de guardar
        for (DetalleVenta detalle : detalles) {
            Producto producto = detalle.getProducto();
            if (producto.getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }
        }
        
        // Guardar venta
        Venta ventaGuardada = guardar(venta);
        
        // Actualizar stock
        for (DetalleVenta detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
            productoService.guardar(producto);
        }
        
        return ventaGuardada;
    }
    
    // ============================================
    // ANULAR VENTA
    // ============================================
    @Transactional
    public void anularVenta(Integer id) {
        Optional<Venta> opt = ventaRepository.findById(id);
        if (opt.isPresent()) {
            Venta venta = opt.get();
            venta.setEstadoVenta("Anulado");
            venta.setFechaActualizacion(LocalDateTime.now());
            
            // Restaurar stock de los productos
            if (venta.getDetalles() != null) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
                    productoService.guardar(producto);
                }
            }
            
            ventaRepository.save(venta);
        }
    }
    
    // ============================================
    // CALCULAR TOTALES
    // ============================================
    private void calcularTotales(Venta venta) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuento = BigDecimal.ZERO;
        
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
                
                BigDecimal precio = detalle.getPrecioUnitario();
                BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
                BigDecimal descuentoUnitario = detalle.getDescuentoUnitario() != null ? 
                                                detalle.getDescuentoUnitario() : BigDecimal.ZERO;
                
                BigDecimal subtotalDetalle = precio.multiply(cantidad);
                BigDecimal descuentoDetalle = descuentoUnitario.multiply(cantidad);
                BigDecimal subtotalConDescuento = subtotalDetalle.subtract(descuentoDetalle);
                
                BigDecimal igvDetalle = subtotalConDescuento.multiply(IGV_PORCENTAJE)
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalDetalle = subtotalConDescuento.add(igvDetalle)
                        .setScale(2, RoundingMode.HALF_UP);
                
                detalle.setSubtotal(subtotalConDescuento.setScale(2, RoundingMode.HALF_UP));
                detalle.setIgv(igvDetalle);
                detalle.setTotal(totalDetalle);
                
                subtotal = subtotal.add(subtotalConDescuento);
                descuento = descuento.add(descuentoDetalle);
            }
        }
        
        BigDecimal igvTotal = subtotal.multiply(IGV_PORCENTAJE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igvTotal)
                .setScale(2, RoundingMode.HALF_UP);
        
        venta.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        venta.setDescuento(descuento.setScale(2, RoundingMode.HALF_UP));
        venta.setIgv(igvTotal);
        venta.setTotal(total);
    }
}