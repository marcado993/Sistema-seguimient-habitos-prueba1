package com.example.dao;

import com.example.model.Objetivo;
import com.example.model.RegistroProgreso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ObjetivoDAO {

    public List<Objetivo> findByUsuarioId(String usuarioId) {
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

    public Optional<Objetivo> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Objetivo objetivo = em.find(Objetivo.class, id);
            return Optional.ofNullable(objetivo);
        } finally {
            em.close();
        }
    }

    public List<Objetivo> findObjetivosActivos(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'ACTIVO' ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Objetivo> findObjetivosCompletados(String usuarioId) {
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
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Log para debugging
            System.out.println("💾 Guardando objetivo: " + objetivo.getTitulo());
            
            if (objetivo.getId() == null) {
                em.persist(objetivo);
                System.out.println("✅ Objetivo persistido con ID: " + objetivo.getId());
            } else {
                objetivo = em.merge(objetivo);
                System.out.println("✅ Objetivo actualizado con ID: " + objetivo.getId());
            }
            
            tx.commit();
            System.out.println("✅ Transacción confirmada");
            return objetivo;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                System.err.println("❌ Transacción revertida");
            }
            System.err.println("❌ Error al guardar objetivo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar objetivo", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Objetivo objetivo = em.find(Objetivo.class, id);
            if (objetivo != null) {
                em.remove(objetivo);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar objetivo", e);
        } finally {
            em.close();
        }
    }

    public void actualizarProgreso(Long objetivoId, int nuevoProgreso, String observaciones) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Objetivo objetivo = em.find(Objetivo.class, objetivoId);
            if (objetivo != null) {
                int progresoAnterior = objetivo.getProgreso();
                
                // Crear registro de progreso
                RegistroProgreso registro = new RegistroProgreso(objetivo, progresoAnterior, nuevoProgreso, observaciones);
                em.persist(registro);
                
                // Actualizar objetivo
                objetivo.actualizarProgreso(nuevoProgreso);
                em.merge(objetivo);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar progreso", e);
        } finally {
            em.close();
        }
    }

    public List<RegistroProgreso> findRegistrosProgreso(Long objetivoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.objetivo.id = :objetivoId ORDER BY r.fechaRegistro DESC", 
                RegistroProgreso.class);
            query.setParameter("objetivoId", objetivoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Long countObjetivosCompletados(String usuarioId) {
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

    public Double getProgresoPromedio(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(o.progreso) FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'ACTIVO'", 
                Double.class);
            query.setParameter("usuarioId", usuarioId);
            Double resultado = query.getSingleResult();
            return resultado != null ? resultado : 0.0;
        } finally {
            em.close();
        }
    }
}
