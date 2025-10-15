package com.ventadeautos.backend.controller;

import com.ventadeautos.backend.model.Usuario;
import com.ventadeautos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            boolean eliminado = usuarioService.eliminarUsuario(id);
            if (eliminado) {
                // ✅ Devolver un objeto JSON en lugar de solo texto
                return ResponseEntity.ok().body(Map.of(
                    "mensaje", "Usuario eliminado correctamente",
                    "eliminado", true
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "mensaje", "Usuario no encontrado",
                    "eliminado", false
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "mensaje", "Error al eliminar usuario: " + e.getMessage(),
                "eliminado", false
            ));
        }
    }

    // ✅ NUEVO: Endpoint para editar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuarioEditado = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuarioEditado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar usuario: " + e.getMessage());
        }
    }



}