// com.lixy.sistemaayd.controller/ProveedorController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Proveedor;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {
    
    @Autowired
    private ProveedorService proveedorService;
    
    // Listar proveedores
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Proveedor> proveedores = proveedorService.listarActivos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("titulo", "Proveedores");
        return "proveedores";
    }
    
    // Mostrar formulario de nuevo proveedor
    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("titulo", "Nuevo Proveedor");
        return "proveedor_form";
    }
    
    // Guardar proveedor
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveedor proveedor, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        proveedorService.guardar(proveedor);
        return "redirect:/proveedores";
    }
    
    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Proveedor proveedor = proveedorService.buscarPorId(id).orElse(null);
        if (proveedor == null) {
            return "redirect:/proveedores";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("titulo", "Editar Proveedor");
        return "proveedor_form";
    }
    
    // Desactivar proveedor
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        proveedorService.desactivar(id);
        return "redirect:/proveedores";
    }
}
