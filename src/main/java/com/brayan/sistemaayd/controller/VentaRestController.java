// com.lixy.sistemaayd/controller/VentaRestController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Venta;
import com.brayan.sistemaayd.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaRestController {
    
    @Autowired
    private VentaService ventaService;
    
    @GetMapping("/reporte-mensual")
    public List<Map<String, Object>> getReporteMensual() {
        List<Venta> ventas = ventaService.getVentasDelMes();
        List<Map<String, Object>> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Venta v : ventas) {
            Map<String, Object> item = new HashMap<>();
            item.put("codigoVenta", v.getCodigoVenta());
            item.put("cliente", v.getCliente() != null ? v.getCliente().getNombreCompleto() : "Cliente General");
            item.put("fecha", v.getFechaVenta().format(formatter));
            item.put("total", v.getTotal());
            item.put("metodoPago", v.getMetodoPago());
            item.put("estado", v.getEstadoVenta());
            resultado.add(item);
        }
        
        return resultado;
    }
}
