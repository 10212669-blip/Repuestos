// com.lixy.sistemaayd/service/DashboardService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.repository.ClienteRepository;
import com.brayan.sistemaayd.repository.ProductoRepository;
import com.brayan.sistemaayd.repository.ProveedorRepository;
import com.brayan.sistemaayd.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    public Map<String, Object> getEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        // Productos
        stats.put("totalProductos", productoService.listarActivos().size());
        stats.put("productosStockBajo", productoService.productosConStockBajo().size());
        
        // Clientes
        stats.put("totalClientes", clienteService.listarActivos().size());
        
        // Proveedores
        stats.put("totalProveedores", proveedorService.listarActivos().size());
        
        // Ventas de hoy
        BigDecimal ventasHoy = ventaService.getTotalVentasHoy();
        stats.put("ventasHoy", ventasHoy != null ? ventasHoy : BigDecimal.ZERO);
        
        Long cantidadVentasHoy = ventaService.getCantidadVentasHoy();
        stats.put("cantidadVentasHoy", cantidadVentasHoy != null ? cantidadVentasHoy : 0L);
        
        // Ventas del mes
        BigDecimal ventasMes = ventaService.getTotalVentasMes();
        stats.put("ventasMes", ventasMes != null ? ventasMes : BigDecimal.ZERO);
        
        return stats;
    }
    
    // Obtener ventas de los últimos 7 días para la gráfica
    public List<Object[]> getVentasUltimos7Dias() {
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(7);
        return ventaRepository.findVentasAgrupadasPorDia(fechaInicio);
    }
}