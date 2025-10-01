package com.example.dao;

import com.example.model.User;
import jakarta.persistence.*;
import java.util.List;

public class UserDAO {
    private EntityManagerFactory emf;

    public UserDAO() {
        try {
            emf = Persistence.createEntityManagerFactory("myPU");
            System.out.println("✓ EntityManagerFactory creado con Hibernate");
        } catch (Exception e) {
            System.err.println("✗ Error al crear EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }

    public void save(User user) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            System.out.println("✓ Usuario guardado: " + user.getEmail());
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("✗ Error al guardar usuario: " + e.getMessage());
            throw new RuntimeException("Error al guardar usuario", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findAll() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            System.out.println("✓ Usuarios encontrados: " + users.size());
            return users;
        } catch (Exception e) {
            System.err.println("✗ Error al buscar usuarios: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al buscar usuarios", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✓ EntityManagerFactory cerrado");
        }
    }
}