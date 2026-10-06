// com.lixy.sistemaayd/controller/UsuarioController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.AuthService;
import com.brayan.sistemaayd.service.BitacoraService;
import com.brayan.sistemaayd.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BitacoraService bitacoraService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ============================================
    // LISTAR USUARIOS
    // ============================================
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Gestión de Usuarios");
        return "usuarios";
    }
    
    // ============================================
    // NUEVO USUARIO
    // ============================================
    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioNuevo", new Usuario());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuario_form";
    }
    
    // ============================================
    // GUARDAR USUARIO (CREAR)
    // ============================================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuarioNuevo,
                          @RequestParam("contrasenaPlana") String contrasenaPlana,
                          HttpSession session,
                          HttpServletRequest request) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        
        // Encriptar contraseña
        usuarioNuevo.setContrasena(passwordEncoder.encode(contrasenaPlana));
        usuarioNuevo.setEstado("Activo");
        
        usuarioService.guardar(usuarioNuevo);
        
        // Bitácora
        bitacoraService.registrar(
            usuarioLogueado.getIdUsuario(),
            "CREAR_USUARIO",
            "Se creó el usuario: " + usuarioNuevo.getUsuario(),
            request
        );
        
        return "redirect:/usuarios";
    }
    
    // ============================================
    // EDITAR USUARIO
    // ============================================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Usuario usuarioEditar = usuarioService.buscarPorId(id).orElse(null);
        if (usuarioEditar == null) {
            return "redirect:/usuarios";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioNuevo", usuarioEditar);
        model.addAttribute("titulo", "Editar Usuario");
        return "usuario_form";
    }
    
    // ============================================
    // ACTUALIZAR USUARIO
    // ============================================
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Usuario usuarioEditado,
                             @RequestParam(value = "contrasenaPlana", required = false) String contrasenaPlana,
                             HttpSession session,
                             HttpServletRequest request) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        
        Usuario usuarioExistente = usuarioService.buscarPorId(usuarioEditado.getIdUsuario()).orElse(null);
        if (usuarioExistente == null) {
            return "redirect:/usuarios";
        }
        
        // Actualizar datos
        usuarioExistente.setUsuario(usuarioEditado.getUsuario());
        usuarioExistente.setEmail(usuarioEditado.getEmail());
        usuarioExistente.setNombreCompleto(usuarioEditado.getNombreCompleto());
        usuarioExistente.setRol(usuarioEditado.getRol());
        usuarioExistente.setEstado(usuarioEditado.getEstado());
        
        // Si se ingresó nueva contraseña, encriptarla
        if (contrasenaPlana != null && !contrasenaPlana.trim().isEmpty()) {
            usuarioExistente.setContrasena(passwordEncoder.encode(contrasenaPlana));
        }
        
        usuarioService.guardar(usuarioExistente);
        
        bitacoraService.registrar(
            usuarioLogueado.getIdUsuario(),
            "EDITAR_USUARIO",
            "Se editó el usuario: " + usuarioExistente.getUsuario(),
            request
        );
        
        return "redirect:/usuarios";
    }
    
    // ============================================
    // CAMBIAR CONTRASEÑA
    // ============================================
    @GetMapping("/cambiar-password/{id}")
    public String cambiarPasswordForm(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Usuario usuarioEditar = usuarioService.buscarPorId(id).orElse(null);
        if (usuarioEditar == null) {
            return "redirect:/usuarios";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioEditar", usuarioEditar);
        model.addAttribute("titulo", "Cambiar Contraseña");
        return "cambiar_password";
    }
    
    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestParam("idUsuario") Integer idUsuario,
                                  @RequestParam("nuevaContrasena") String nuevaContrasena,
                                  @RequestParam("confirmarContrasena") String confirmarContrasena,
                                  HttpSession session,
                                  HttpServletRequest request,
                                  Model model) {
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        
        // Validar que las contraseñas coincidan
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "❌ Las contraseñas no coinciden");
            model.addAttribute("usuario", usuarioLogueado);
            model.addAttribute("usuarioEditar", usuarioService.buscarPorId(idUsuario).orElse(null));
            model.addAttribute("titulo", "Cambiar Contraseña");
            return "cambiar_password";
        }
        
        // Validar longitud mínima
        if (nuevaContrasena.length() < 6) {
            model.addAttribute("error", "❌ La contraseña debe tener al menos 6 caracteres");
            model.addAttribute("usuario", usuarioLogueado);
            model.addAttribute("usuarioEditar", usuarioService.buscarPorId(idUsuario).orElse(null));
            model.addAttribute("titulo", "Cambiar Contraseña");
            return "cambiar_password";
        }
        
        Usuario usuarioEditar = usuarioService.buscarPorId(idUsuario).orElse(null);
        if (usuarioEditar == null) {
            return "redirect:/usuarios";
        }
        
        // Encriptar y guardar
        usuarioEditar.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioService.guardar(usuarioEditar);
        
        bitacoraService.registrar(
            usuarioLogueado.getIdUsuario(),
            "CAMBIAR_PASSWORD",
            "Se cambió la contraseña del usuario: " + usuarioEditar.getUsuario(),
            request
        );
        
        return "redirect:/usuarios?passwordCambiada=true";
    }
    
    // ============================================
    // ENCRIPTAR TODAS LAS CONTRASEÑAS EXISTENTES
    // ============================================
    @GetMapping("/encriptar-todas")
    public String encriptarTodas(HttpSession session, HttpServletRequest request) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        
        List<Usuario> usuarios = usuarioService.listarTodos();
        int contador = 0;
        
        for (Usuario u : usuarios) {
            // Si la contraseña NO empieza con $2a$ (no es BCrypt)
            if (u.getContrasena() != null && !u.getContrasena().startsWith("$2a$")) {
                // Encriptar la contraseña actual (texto plano)
                String passwordEncriptada = passwordEncoder.encode(u.getContrasena());
                u.setContrasena(passwordEncriptada);
                usuarioService.guardar(u);
                contador++;
                System.out.println("🔐 Encriptada contraseña de: " + u.getUsuario());
            }
        }
        
        bitacoraService.registrar(
            usuarioLogueado.getIdUsuario(),
            "ENCRIPTAR_PASSWORDS",
            "Se encriptaron " + contador + " contraseñas",
            request
        );
        
        return "redirect:/usuarios?encriptadas=" + contador;
    }
    
    // ============================================
    // ELIMINAR USUARIO
    // ============================================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session, HttpServletRequest request) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        
        Usuario usuarioEliminar = usuarioService.buscarPorId(id).orElse(null);
        if (usuarioEliminar != null) {
            usuarioEliminar.setEstado("Inactivo");
            usuarioService.guardar(usuarioEliminar);
            
            bitacoraService.registrar(
                usuarioLogueado.getIdUsuario(),
                "DESACTIVAR_USUARIO",
                "Se desactivó el usuario: " + usuarioEliminar.getUsuario(),
                request
            );
        }
        
        return "redirect:/usuarios";
    }
}