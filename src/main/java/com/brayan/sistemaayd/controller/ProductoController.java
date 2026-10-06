// com.lixy.sistemaayd.controller/ProductoController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Categoria;
import com.brayan.sistemaayd.entity.Marca;
import com.brayan.sistemaayd.entity.Producto;
import com.brayan.sistemaayd.entity.Proveedor;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.CategoriaService;
import com.brayan.sistemaayd.service.MarcaService;
import com.brayan.sistemaayd.service.ProductoService;
import com.brayan.sistemaayd.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    // Listar productos
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Producto> productos = productoService.listarActivos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Productos");
        return "productos";
    }
    
    // Mostrar formulario de nuevo producto
    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarActivos());
        model.addAttribute("marcas", marcaService.listarActivos());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("titulo", "Nuevo Producto");
        return "producto_form";
    }
    
    // Guardar producto
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        productoService.guardar(producto);
        return "redirect:/productos";
    }
    
    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Producto producto = productoService.buscarPorId(id).orElse(null);
        if (producto == null) {
            return "redirect:/productos";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarActivos());
        model.addAttribute("marcas", marcaService.listarActivos());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("titulo", "Editar Producto");
        return "producto_form";
    }
    
    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.desactivar(id);
        return "redirect:/productos";
    }
}
