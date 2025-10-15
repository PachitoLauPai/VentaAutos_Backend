// UsuarioService.java
package com.ventadeautos.backend.service;

import com.ventadeautos.backend.dto.LoginRequest;
import com.ventadeautos.backend.dto.LoginResponse;
import com.ventadeautos.backend.dto.RegistroRequest;
import com.ventadeautos.backend.model.Usuario;
import com.ventadeautos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        
        if (usuarioOpt.isPresent() && 
            usuarioOpt.get().getPassword().equals(request.getPassword())) {
            
            Usuario usuario = usuarioOpt.get();
            LoginResponse response = new LoginResponse();
            response.setId(usuario.getId());
            response.setEmail(usuario.getEmail());
            response.setNombre(usuario.getNombre());
            response.setRol(usuario.getRol());
            response.setMensaje("Login exitoso");
            return response;
        }
        
        LoginResponse response = new LoginResponse();
        response.setMensaje("Credenciales inválidas");
        return response;
    }
    
    public LoginResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            LoginResponse response = new LoginResponse();
            response.setMensaje("El email ya está registrado");
            return response;
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setNombre(request.getNombre());
        usuario.setRol(request.getRol());
        
        usuarioRepository.save(usuario);
        
        LoginResponse response = new LoginResponse();
        response.setId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setNombre(usuario.getNombre());
        response.setRol(usuario.getRol());
        response.setMensaje("Usuario registrado exitosamente");
        return response;
    }

    // ✅ CORREGIDO: Agregar punto y coma
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }



   // En UsuarioService, agregar este método:
    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // En UsuarioService, agregar este método:
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                // Actualizar solo los campos que vienen en el request
                if (usuarioActualizado.getNombre() != null) {
                    usuario.setNombre(usuarioActualizado.getNombre());
                }
                if (usuarioActualizado.getEmail() != null) {
                    // Verificar que el email no esté en uso por otro usuario
                    if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) && 
                        usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
                        throw new RuntimeException("El email ya está en uso por otro usuario");
                    }
                    usuario.setEmail(usuarioActualizado.getEmail());
                }
                if (usuarioActualizado.getRol() != null) {
                    usuario.setRol(usuarioActualizado.getRol());
                }
                // La contraseña no se actualiza aquí por seguridad
                
                return usuarioRepository.save(usuario);
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

}