// com.lixy.sistemaayd/controller/PasswordController.java
package com.brayan.sistemaayd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/generar-password")
    @ResponseBody
    public String generarPassword(@RequestParam String texto) {
        String encriptada = passwordEncoder.encode(texto);
        return "<h2>🔐 Generador de Contraseñas BCrypt</h2>" +
               "<p><strong>Contraseña original:</strong> " + texto + "</p>" +
               "<p><strong>Contraseña encriptada:</strong></p>" +
               "<pre style='background:#f0f0f0; padding:10px; border-radius:5px;'>" + encriptada + "</pre>" +
               "<p><strong>SQL para actualizar:</strong></p>" +
               "<pre style='background:#f0f0f0; padding:10px; border-radius:5px;'>UPDATE tienda.usuarios SET contrasena = '" + encriptada + "' WHERE usuario = 'admin';</pre>";
    }
}
