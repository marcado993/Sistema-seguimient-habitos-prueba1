package com.example.dao;

import com.example.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para gestión de Usuarios
 * Nota: Usando modelo simple sin JPA por ahora
 */
public class UsuarioDAO {
    
    private List<Usuario> usuarios;
    
    public UsuarioDAO() {
        this.usuarios = new ArrayList<>();
        // Agregar usuario demo por defecto
        Usuario demo = new Usuario("Usuario Demo", "demo@ejemplo.com", "demo123");
        usuarios.add(demo);
        System.out.println("✓ UsuarioDAO inicializado");
    }
    
    /**
     * Guardar un nuevo usuario
     */
    public Usuario save(Usuario usuario) {
        try {
            if (usuario != null) {
                usuarios.add(usuario);
                System.out.println("✓ Usuario guardado: " + usuario.getCorreo());
                return usuario;
            }
            return null;
        } catch (Exception e) {
            System.err.println("✗ Error al guardar usuario: " + e.getMessage());
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> findAll() {
        try {
            System.out.println("✓ Usuarios encontrados: " + usuarios.size());
            return new ArrayList<>(usuarios);
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuarios: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al buscar usuarios", e);
        }
    }
    
    /**
     * Buscar usuario por correo
     */
    public Optional<Usuario> findByCorreo(String correo) {
        try {
            return usuarios.stream()
                    .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuario por correo: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Buscar usuario por nombre
     */
    public Optional<Usuario> findByNombre(String nombre) {
        try {
            return usuarios.stream()
                    .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuario por nombre: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Validar credenciales de usuario
     */
    public boolean validarCredenciales(String correo, String contrasena) {
        try {
            Optional<Usuario> usuario = findByCorreo(correo);
            if (usuario.isPresent()) {
                return usuario.get().getContrasena().equals(contrasena);
            }
            return false;
        } catch (Exception e) {
            System.err.println("✗ Error al validar credenciales: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Eliminar usuario por correo
     */
    public boolean delete(String correo) {
        try {
            return usuarios.removeIf(u -> u.getCorreo().equalsIgnoreCase(correo));
        } catch (Exception e) {
            System.err.println("✗ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
