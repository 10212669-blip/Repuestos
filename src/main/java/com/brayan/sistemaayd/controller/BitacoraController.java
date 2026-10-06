// com.lixy.sistemaayd/controller/BitacoraController.java
package com.brayan.sistemaayd.controller;

import com.brayan.sistemaayd.entity.LogUsuario;
import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.service.BitacoraService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/bitacora")
public class BitacoraController {
    
    @Autowired
    private BitacoraService bitacoraService;
    
    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<LogUsuario> logs = bitacoraService.listarUltimos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("logs", logs);
        model.addAttribute("titulo", "Bitácora del Sistema");
        return "bitacora";
    }
}