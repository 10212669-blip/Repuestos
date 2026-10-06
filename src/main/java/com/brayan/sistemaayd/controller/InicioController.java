// com.lixy.sistemaayd/controller/InicioController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.AuthService;
import com.brayan.sistemaayd.service.BitacoraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InicioController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BitacoraService bitacoraService;
    
    @GetMapping("/")
    public String index() {
        return "login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("usuario") String usuario,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            HttpServletRequest request,
            Model model) {
        
        if (usuario == null || usuario.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            model.addAttribute("error", "⚠️ Usuario y contraseña son requeridos");
            return "login";
        }
        
        if (authService.autenticar(usuario, contrasena, request)) {
            Usuario user = authService.getUsuarioByUsername(usuario).orElse(null);
            if (user != null) {
                session.setAttribute("usuario", user);
                session.setAttribute("nombre", user.getNombreCompleto());
                session.setAttribute("rol", user.getRol());
                return "redirect:/dashboard";
            }
        }
        
        model.addAttribute("error", "❌ Usuario o contraseña incorrectos");
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            bitacoraService.registrar(
                usuario.getIdUsuario(),
                "LOGOUT",
                "El usuario " + usuario.getUsuario() + " cerró sesión",
                request
            );
        }
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}