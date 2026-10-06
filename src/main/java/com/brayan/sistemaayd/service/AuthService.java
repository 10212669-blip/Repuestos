// com.lixy.sistemaayd/service/AuthService.java
package com.brayan.sistemaayd.service;

import com.brayan.sistemaayd.entity.Usuario;
import com.brayan.sistemaayd.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BitacoraService bitacoraService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public boolean autenticar(String usuario, String contrasena, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);
            
            if (usuarioOpt.isPresent()) {
                Usuario user = usuarioOpt.get();
                
                boolean passwordCorrecta = false;
                
                try {
                    // ✅ Verificar con BCrypt
                    passwordCorrecta = passwordEncoder.matches(contrasena, user.getContrasena());
                } catch (Exception e) {
                    // Si la contraseña está en texto plano (migración)
                    passwordCorrecta = contrasena.equals(user.getContrasena());
                    
                    if (passwordCorrecta) {
                        // Encriptar automáticamente
                        user.setContrasena(passwordEncoder.encode(contrasena));
                        usuarioRepository.save(user);
                        System.out.println("🔐 Contraseña migrada a BCrypt: " + usuario);
                    }
                }
                
                if (passwordCorrecta) {
                    user.setUltimoAcceso(LocalDateTime.now());
                    usuarioRepository.save(user);
                    
                    bitacoraService.registrar(
                        user.getIdUsuario(),
                        "LOGIN",
                        "El usuario " + user.getUsuario() + " inició sesión",
                        request
                    );
                    
                    System.out.println("✅ Login exitoso: " + usuario);
                    return true;
                }
            }
            
            // Login fallido
            bitacoraService.registrar(
                null,
                "LOGIN_FALLIDO",
                "Intento fallido de inicio de sesión: " + usuario,
                request
            );
            System.out.println("❌ Login fallido: " + usuario);
            return false;
            
        } catch (Exception e) {
            System.err.println("Error en autenticación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Optional<Usuario> getUsuarioByUsername(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }
    
    // Método para encriptar contraseñas
    public String encriptarContrasena(String contrasena) {
        return passwordEncoder.encode(contrasena);
    }
}