package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Habito;
import com.sistema_seguimiento.model.RegistroHabito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DAO para gestión de Hábitos
 * 
 * Refactorizado para usar BaseDAO y eliminar código repetitivo
 * de gestión de transacciones.
 */
public class HabitoDAO extends BaseDAO {

    public List<Habito> findByUsuarioId(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Habito> query = em.createQuery(
                "SELECT DISTINCT h FROM Habito h " +
                "LEFT JOIN FETCH h.registros " +
                "WHERE h.usuarioId = :usuarioId AND h.activo = true " +
                "ORDER BY h.fechaCreacion DESC", 
                Habito.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Habito> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Habito habito = em.find(Habito.class, id);
            return Optional.ofNullable(habito);
        } finally {
            em.close();
        }
    }

    public List<Habito> findHabitosActivos(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Habito> query = em.createQuery(
                "SELECT DISTINCT h FROM Habito h " +
                "LEFT JOIN FETCH h.registros " +
                "WHERE h.usuarioId = :usuarioId AND h.activo = true " +
                "ORDER BY h.nombre", 
                Habito.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Habito save(Habito habito) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Log para debugging
            System.out.println("💾 Guardando hábito: " + habito.getNombre());
            if (habito.getObjetivo() != null) {
                System.out.println("🎯 Asociado al objetivo ID: " + habito.getObjetivo().getId());
                // Asegurar que el objetivo esté en el contexto de persistencia
                if (!em.contains(habito.getObjetivo())) {
                    habito.setObjetivo(em.merge(habito.getObjetivo()));
                }
            }
            
            if (habito.getId() == null) {
                em.persist(habito);
                System.out.println("✅ Hábito persistido con ID: " + habito.getId());
            } else {
                habito = em.merge(habito);
                System.out.println("✅ Hábito actualizado con ID: " + habito.getId());
            }
            
            tx.commit();
            System.out.println("✅ Transacción confirmada");
            return habito;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
                System.err.println("❌ Transacción revertida");
            }
            System.err.println("❌ Error al guardar hábito: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar hábito", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Habito habito = em.find(Habito.class, id);
            if (habito != null) {
                habito.setActivo(false); // Soft delete
                em.merge(habito);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar hábito", e);
        } finally {
            em.close();
        }
    }

    public RegistroHabito findRegistroByFecha(Long habitoId, LocalDate fecha) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroHabito> query = em.createQuery(
                "SELECT r FROM RegistroHabito r WHERE r.habito.id = :habitoId AND r.fecha = :fecha", 
                RegistroHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("fecha", fecha);
            List<RegistroHabito> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    public RegistroHabito saveRegistro(RegistroHabito registro) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (registro.getId() == null) {
                em.persist(registro);
            } else {
                registro = em.merge(registro);
            }
            tx.commit();
            return registro;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar registro", e);
        } finally {
            em.close();
        }
    }

    public void registrarCompletado(Long habitoId, LocalDate fecha, String observacion) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Habito habito = em.find(Habito.class, habitoId);
            if (habito == null) {
                throw new IllegalArgumentException("Hábito no encontrado");
            }

            // Buscar registro existente
            TypedQuery<RegistroHabito> query = em.createQuery(
                "SELECT r FROM RegistroHabito r WHERE r.habito.id = :habitoId AND r.fecha = :fecha", 
                RegistroHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("fecha", fecha);
            
            List<RegistroHabito> existentes = query.getResultList();
            RegistroHabito registro;
            
            if (existentes.isEmpty()) {
                // Crear nuevo registro
                registro = new RegistroHabito(habito, fecha, 1, observacion);
                em.persist(registro);
            } else {
                // Incrementar existente
                registro = existentes.get(0);
                registro.setCompletado(registro.getCompletado() + 1);
                if (observacion != null && !observacion.trim().isEmpty()) {
                    registro.setObservacion(observacion);
                }
                em.merge(registro);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al registrar completado", e);
        } finally {
            em.close();
        }
    }

    public List<RegistroHabito> findRegistrosByRango(Long habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroHabito> query = em.createQuery(
                "SELECT r FROM RegistroHabito r WHERE r.habito.id = :habitoId AND r.fecha BETWEEN :inicio AND :fin ORDER BY r.fecha DESC", 
                RegistroHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<RegistroHabito> findRegistrosDeHoy(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<RegistroHabito> query = em.createQuery(
                "SELECT r FROM RegistroHabito r WHERE r.habito.usuarioId = :usuarioId AND r.fecha = :hoy", 
                RegistroHabito.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("hoy", LocalDate.now());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Long countHabitosCompletadosHoy(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM RegistroHabito r WHERE r.habito.usuarioId = :usuarioId AND r.fecha = :hoy AND r.completado >= r.habito.metaDiaria", 
                Long.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("hoy", LocalDate.now());
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Double getPorcentajeCompletadoSemana(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate inicioSemana = hoy.minusDays(6); // Últimos 7 días

            // Total de hábitos activos * 7 días
            TypedQuery<Long> queryTotal = em.createQuery(
                "SELECT COUNT(h) FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true", 
                Long.class);
            queryTotal.setParameter("usuarioId", usuarioId);
            Long habitosActivos = queryTotal.getSingleResult();
            
            if (habitosActivos == 0) return 0.0;

            // Hábitos completados en la semana
            TypedQuery<Long> queryCompletados = em.createQuery(
                "SELECT COUNT(r) FROM RegistroHabito r WHERE r.habito.usuarioId = :usuarioId AND r.fecha BETWEEN :inicio AND :fin AND r.completado >= r.habito.metaDiaria", 
                Long.class);
            queryCompletados.setParameter("usuarioId", usuarioId);
            queryCompletados.setParameter("inicio", inicioSemana);
            queryCompletados.setParameter("fin", hoy);
            Long completados = queryCompletados.getSingleResult();

            return (completados * 100.0) / (habitosActivos * 7);
        } finally {
            em.close();
        }
    }
}
