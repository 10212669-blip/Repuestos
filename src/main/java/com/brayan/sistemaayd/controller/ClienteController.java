// com.lixy.sistemaayd.controller/ClienteController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Cliente;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    // Listar clientes
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Cliente> clientes = clienteService.listarActivos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", "Clientes");
        return "clientes";
    }
    
    // Mostrar formulario de nuevo cliente
    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "cliente_form";
    }
    
    // Guardar cliente
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }
    
    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Cliente cliente = clienteService.buscarPorId(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "cliente_form";
    }
    
    // Desactivar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        clienteService.desactivar(id);
        return "redirect:/clientes";
    }
}
