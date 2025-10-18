package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO con JPA para gestión de Usuarios compatible con Supabase
 * Usa EntityManager para operaciones de base de datos
 */
public class UsuarioDAOJPA {
    
    /**
     * Guardar un nuevo usuario
     */
    public Usuario save(Usuario usuario) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = null;
        
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            em.persist(usuario);
            
            transaction.commit();
            System.out.println("✓ Usuario guardado: " + usuario.getCorreo());
            return usuario;
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("✗ Error al guardar usuario: " + e.getMessage());
            throw new RuntimeException("Error al guardar usuario", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar usuario por ID
     */
    public Optional<Usuario> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuario por ID: " + e.getMessage());
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar usuario por correo
     */
    public Optional<Usuario> findByCorreo(String correo) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo", 
                Usuario.class
            );
            query.setParameter("correo", correo);
            
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
            
        } catch (Exception e) {
            System.err.println("✗ Usuario no encontrado con correo: " + correo);
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u ORDER BY u.nombre", 
                Usuario.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuarios: " + e.getMessage());
            throw new RuntimeException("Error al buscar usuarios", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualizar usuario existente
     */
    public Usuario update(Usuario usuario) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = null;
        
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            Usuario updated = em.merge(usuario);
            
            transaction.commit();
            System.out.println("✓ Usuario actualizado: " + usuario.getCorreo());
            return updated;
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("✗ Error al actualizar usuario: " + e.getMessage());
            throw new RuntimeException("Error al actualizar usuario", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Eliminar usuario por ID
     */
    public boolean delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = null;
        
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
                transaction.commit();
                System.out.println("✓ Usuario eliminado: " + id);
                return true;
            } else {
                System.out.println("✗ Usuario no encontrado: " + id);
                return false;
            }
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("✗ Error al eliminar usuario: " + e.getMessage());
            throw new RuntimeException("Error al eliminar usuario", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Validar credenciales de usuario
     */
    public boolean validarCredenciales(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = findByCorreo(correo);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // TODO: En producción, usar BCrypt para comparar passwords hasheadas
            return usuario.getContrasena().equals(contrasena);
        }
        
        return false;
    }
    
    /**
     * Verificar si existe un usuario con el correo dado
     */
    public boolean existsByCorreo(String correo) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.correo = :correo", 
                Long.class
            )
            .setParameter("correo", correo)
            .getSingleResult();
            
            return count > 0;
            
        } catch (Exception e) {
            System.err.println("✗ Error al verificar existencia de usuario: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar usuarios por nombre (búsqueda parcial)
     */
    public List<Usuario> searchByNombre(String nombreParcial) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(:nombre) ORDER BY u.nombre", 
                Usuario.class
            );
            query.setParameter("nombre", "%" + nombreParcial + "%");
            
            return query.getResultList();
            
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuarios por nombre: " + e.getMessage());
            throw new RuntimeException("Error al buscar usuarios", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Contar total de usuarios
     */
    public long count() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("✗ Error al contar usuarios: " + e.getMessage());
            return 0L;
        } finally {
            em.close();
        }
    }
}
