package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.RegistroProgreso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para gestión de Objetivos
 * 
 * Proporciona operaciones CRUD y consultas específicas para la gestión
 * de objetivos y su progreso.
 */
public class ObjetivoDAO {

    public List<Objetivo> findByUsuarioId(Integer usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Objetivo> findById(Integer id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Objetivo objetivo = em.find(Objetivo.class, id);
            return Optional.ofNullable(objetivo);
        } finally {
            em.close();
        }
    }

    public List<Objetivo> findObjetivosActivos(Integer usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'PENDIENTE' ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Objetivo> findObjetivosCompletados(Integer usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'COMPLETADO' ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Objetivo save(Objetivo objetivo) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            System.out.println("💾 Guardando objetivo: " + objetivo.getTitulo());
            
            Objetivo result;
            if (objetivo.getId() == null) {
                em.persist(objetivo);
                System.out.println("✅ Objetivo persistido con ID: " + objetivo.getId());
                result = objetivo;
            } else {
                result = em.merge(objetivo);
                System.out.println("✅ Objetivo actualizado con ID: " + result.getId());
            }
            
            transaction.commit();
            System.out.println("✅ Transacción confirmada");
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al guardar objetivo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Objetivo objetivo = em.find(Objetivo.class, id);
            if (objetivo != null) {
                em.remove(objetivo);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al eliminar objetivo: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizarProgreso(Integer objetivoId, int nuevoProgreso, String observaciones) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Objetivo objetivo = em.find(Objetivo.class, objetivoId);
            if (objetivo != null) {
                int progresoAnterior = objetivo.getProgresoActual();
                
                // Crear registro de progreso
                RegistroProgreso registro = new RegistroProgreso(objetivo, progresoAnterior, nuevoProgreso, observaciones);
                em.persist(registro);
                
                // Actualizar objetivo
                objetivo.actualizarProgreso(nuevoProgreso);
                em.merge(objetivo);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error al actualizar progreso: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public List<RegistroProgreso> findRegistrosProgreso(Integer objetivoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.objetivo.id = :objetivoId ORDER BY r.fechaCreacion DESC", 
                RegistroProgreso.class);
            query.setParameter("objetivoId", objetivoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Long countObjetivosCompletados(Integer usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(o) FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'COMPLETADO'", 
                Long.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Double getProgresoPromedio(Integer usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(o.progresoActual) FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'PENDIENTE'", 
                Double.class);
            query.setParameter("usuarioId", usuarioId);
            Double resultado = query.getSingleResult();
            return resultado != null ? resultado : 0.0;
        } finally {
            em.close();
        }
    }
}

