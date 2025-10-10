package com.example.dao;

import com.example.model.RegistroProgreso;
import com.example.model.Habito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RegistroProgresoDAO {
    
    /**
     * Guarda un nuevo registro de progreso
     */
    public void guardar(RegistroProgreso registro) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(registro);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar registro de progreso", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualiza un registro de progreso existente
     */
    public void actualizar(RegistroProgreso registro) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(registro);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar registro de progreso", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene todos los registros de progreso de un hábito
     */
    public List<RegistroProgreso> obtenerPorHabito(Long habitoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.habito.id = :habitoId ORDER BY r.fecha DESC",
                RegistroProgreso.class
            );
            query.setParameter("habitoId", habitoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene un registro de progreso por ID
     */
    public RegistroProgreso obtenerPorId(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return em.find(RegistroProgreso.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene todos los registros de progreso
     */
    public List<RegistroProgreso> obtenerTodos() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r ORDER BY r.fecha DESC",
                RegistroProgreso.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina un registro de progreso
     */
    public void eliminar(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            RegistroProgreso registro = em.find(RegistroProgreso.class, id);
            if (registro != null) {
                em.remove(registro);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar registro de progreso", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Cuenta los registros cumplidos de un hábito
     */
    public long contarCumplidos(Long habitoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM RegistroProgreso r WHERE r.habito.id = :habitoId AND r.cumplido = true",
                Long.class
            );
            query.setParameter("habitoId", habitoId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
