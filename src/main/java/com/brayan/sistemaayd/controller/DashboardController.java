// com.lixy.sistemaayd/controller/DashboardController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.DashboardService;
import com.brayan.sistemaayd.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @Autowired
    private VentaService ventaService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Map<String, Object> stats = dashboardService.getEstadisticas();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("stats", stats);
        model.addAttribute("titulo", "Dashboard");
        return "dashboard";
    }
    
    // ============================================
    // API PARA LA GRÁFICA - VENTAS ÚLTIMOS 7 DÍAS
    // ============================================
    @GetMapping("/api/ventas-7dias")
    @ResponseBody
    public List<Map<String, Object>> getVentas7Dias() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        
        try {
            // Obtener datos agrupados por día
            List<Object[]> datos = ventaService.getVentasUltimos7Dias();
            
            if (datos != null && !datos.isEmpty()) {
                for (Object[] fila : datos) {
                    Map<String, Object> item = new HashMap<>();
                    
                    // Formatear el día
                    String dia = "";
                    if (fila[0] != null) {
                        if (fila[0] instanceof java.sql.Date) {
                            java.sql.Date fecha = (java.sql.Date) fila[0];
                            dia = new java.text.SimpleDateFormat("EEE", new java.util.Locale("es", "ES"))
                                    .format(fecha);
                        } else {
                            dia = fila[0].toString();
                        }
                    }
                    
                    item.put("dia", dia);
                    item.put("total", fila[1] != null ? fila[1] : 0);
                    resultado.add(item);
                }
            } else {
                // Si no hay datos, devolver los últimos 7 días con 0
                resultado = generarDiasVacios();
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener ventas de 7 días: " + e.getMessage());
            e.printStackTrace();
            resultado = generarDiasVacios();
        }
        
        return resultado;
    }
    
    // Generar días vacíos si no hay ventas
    private List<Map<String, Object>> generarDiasVacios() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        
        for (String dia : dias) {
            Map<String, Object> item = new HashMap<>();
            item.put("dia", dia);
            item.put("total", 0);
            resultado.add(item);
        }
        
        return resultado;
    }
}