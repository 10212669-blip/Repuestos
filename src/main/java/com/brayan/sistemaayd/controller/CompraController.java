// com.lixy.sistemaayd/controller/CompraController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Compra;
import com.brayan.sistemaayd.entity.DetalleCompra;
import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.entity.Proveedor;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.CompraService;
import com.brayan.sistemaayd.service.ProductoService;
import com.brayan.sistemaayd.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    // ===== LISTAR COMPRAS =====
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Compra> compras = compraService.listarTodas();
        model.addAttribute("usuario", usuario);
        model.addAttribute("compras", compras);
        model.addAttribute("titulo", "Compras");
        return "compras";
    }
    
    // ===== NUEVA COMPRA =====
    @GetMapping("/nuevo")
    public String nuevaCompra(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("compra", new Compra());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("titulo", "Nueva Compra");
        return "compra_form";
    }
    
    // ===== GUARDAR COMPRA =====
    @PostMapping("/guardar")
    public String guardarCompra(@ModelAttribute Compra compra,
                               @RequestParam("productos") String[] productos,
                               @RequestParam("cantidades") String[] cantidades,
                               @RequestParam("precios") String[] precios,
                               HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        compra.setUsuario(usuario);
        
        List<DetalleCompra> detalles = new ArrayList<>();
        for (int i = 0; i < productos.length; i++) {
            if (productos[i] != null && !productos[i].isEmpty()) {
                DetalleCompra detalle = new DetalleCompra();
                Producto producto = productoService.buscarPorId(Integer.parseInt(productos[i])).orElse(null);
                if (producto != null) {
                    detalle.setProducto(producto);
                    detalle.setCantidad(Integer.parseInt(cantidades[i]));
                    detalle.setPrecioUnitario(new BigDecimal(precios[i]));
                    detalles.add(detalle);
                }
            }
        }
        
        compraService.registrarCompra(compra, detalles);
        return "redirect:/compras";
    }
    
    // ===== VER DETALLE DE COMPRA =====
    @GetMapping("/ver/{id}")
    public String verCompra(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Compra compra = compraService.buscarPorId(id).orElse(null);
        if (compra == null) {
            return "redirect:/compras";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("compra", compra);
        model.addAttribute("titulo", "Detalle de Compra");
        return "compra_detalle";
    }
    
    // ===== ANULAR COMPRA =====
    @GetMapping("/anular/{id}")
    public String anularCompra(@PathVariable Integer id) {
        compraService.anularCompra(id);
        return "redirect:/compras";
    }
}
