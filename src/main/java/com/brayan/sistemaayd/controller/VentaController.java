// com.lixy.sistemaayd/controller/VentaController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.DetalleVenta;
import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.entity.Venta;
import com.brayan.sistemaayd.service.ClienteService;
import com.brayan.sistemaayd.service.ComprobanteService;
import com.brayan.sistemaayd.service.ProductoService;
import com.brayan.sistemaayd.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ComprobanteService comprobanteService;

    // ============================================
    // LISTAR VENTAS
    // ============================================
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Venta> ventas = ventaService.listarTodas();
        model.addAttribute("usuario", usuario);
        model.addAttribute("ventas", ventas);
        model.addAttribute("titulo", "Ventas");
        return "ventas";
    }

    // ============================================
    // NUEVA VENTA
    // ============================================
    @GetMapping("/nuevo")
    public String nuevaVenta(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("detalles", new ArrayList<DetalleVenta>());
        model.addAttribute("titulo", "Nueva Venta");
        return "venta_form";
    }

    // ============================================
    // GUARDAR VENTA
    // ============================================
    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta,
                               @RequestParam("productos") String[] productos,
                               @RequestParam("cantidades") String[] cantidades,
                               @RequestParam("precios") String[] precios,
                               HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        venta.setUsuario(usuario);

        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < productos.length; i++) {
            if (productos[i] != null && !productos[i].isEmpty()) {
                DetalleVenta detalle = new DetalleVenta();
                Producto producto = productoService.buscarPorId(Integer.parseInt(productos[i])).orElse(null);
                if (producto != null) {
                    detalle.setProducto(producto);
                    detalle.setCantidad(Integer.parseInt(cantidades[i]));
                    detalle.setPrecioUnitario(new BigDecimal(precios[i]));
                    detalles.add(detalle);
                }
            }
        }

        ventaService.registrarVenta(venta, detalles);
        return "redirect:/ventas";
    }

    // ============================================
    // VER DETALLE DE VENTA
    // ============================================
    @GetMapping("/ver/{id}")
    public String verVenta(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Venta venta = ventaService.buscarPorId(id).orElse(null);
        
        if (venta == null) {
            model.addAttribute("error", "Venta no encontrada con ID: " + id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("ventas", ventaService.listarTodas());
            model.addAttribute("titulo", "Ventas");
            return "ventas";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("venta", venta);
        model.addAttribute("titulo", "Detalle de Venta");
        return "venta_detalle";
    }

    // ============================================
    // ANULAR VENTA
    // ============================================
    @GetMapping("/anular/{id}")
    public String anularVenta(@PathVariable Integer id) {
        ventaService.anularVenta(id);
        return "redirect:/ventas";
    }

    // ============================================
    // GENERAR COMPROBANTE PDF
    // ============================================
    @GetMapping("/comprobante/{id}")
    public ResponseEntity<byte[]> generarComprobante(@PathVariable Integer id) {
        try {
            Venta venta = ventaService.buscarPorId(id).orElse(null);
            if (venta == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] pdf = comprobanteService.generarComprobantePdf(venta);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("comprobante_" + venta.getCodigoVenta() + ".pdf")
                    .build());

            return ResponseEntity.ok().headers(headers).body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}