package com.example.dao;

import com.example.model.Habito;
import com.example.model.SeguimientoHabito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HabitoDAO {

    public List<Habito> findByUsuarioId(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Habito> query = em.createQuery(
                "SELECT h FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true ORDER BY h.fechaCreacion DESC", 
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
                "SELECT h FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true ORDER BY h.nombre", 
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
            if (habito.getId() == null) {
                em.persist(habito);
            } else {
                habito = em.merge(habito);
            }
            tx.commit();
            return habito;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
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

    public SeguimientoHabito findSeguimientoByFecha(Long habitoId, LocalDate fecha) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoHabito> query = em.createQuery(
                "SELECT s FROM SeguimientoHabito s WHERE s.habito.id = :habitoId AND s.fecha = :fecha", 
                SeguimientoHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("fecha", fecha);
            List<SeguimientoHabito> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    public SeguimientoHabito saveSeguimiento(SeguimientoHabito seguimiento) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (seguimiento.getId() == null) {
                em.persist(seguimiento);
            } else {
                seguimiento = em.merge(seguimiento);
            }
            tx.commit();
            return seguimiento;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar seguimiento", e);
        } finally {
            em.close();
        }
    }

    public void registrarCompletado(Long habitoId, LocalDate fecha, String notas) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Habito habito = em.find(Habito.class, habitoId);
            if (habito == null) {
                throw new IllegalArgumentException("Hábito no encontrado");
            }

            // Buscar seguimiento existente
            TypedQuery<SeguimientoHabito> query = em.createQuery(
                "SELECT s FROM SeguimientoHabito s WHERE s.habito.id = :habitoId AND s.fecha = :fecha", 
                SeguimientoHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("fecha", fecha);
            
            List<SeguimientoHabito> existentes = query.getResultList();
            SeguimientoHabito seguimiento;
            
            if (existentes.isEmpty()) {
                // Crear nuevo seguimiento
                seguimiento = new SeguimientoHabito(habito, fecha, 1, notas);
                em.persist(seguimiento);
            } else {
                // Incrementar existente
                seguimiento = existentes.get(0);
                seguimiento.incrementarCompletado();
                if (notas != null && !notas.trim().isEmpty()) {
                    seguimiento.setNotas(notas);
                }
                em.merge(seguimiento);
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

    public List<SeguimientoHabito> findSeguimientosByRango(Long habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoHabito> query = em.createQuery(
                "SELECT s FROM SeguimientoHabito s WHERE s.habito.id = :habitoId AND s.fecha BETWEEN :inicio AND :fin ORDER BY s.fecha DESC", 
                SeguimientoHabito.class);
            query.setParameter("habitoId", habitoId);
            query.setParameter("inicio", fechaInicio);
            query.setParameter("fin", fechaFin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<SeguimientoHabito> findSeguimientosDeHoy(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<SeguimientoHabito> query = em.createQuery(
                "SELECT s FROM SeguimientoHabito s WHERE s.habito.usuarioId = :usuarioId AND s.fecha = :hoy", 
                SeguimientoHabito.class);
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
                "SELECT COUNT(s) FROM SeguimientoHabito s WHERE s.habito.usuarioId = :usuarioId AND s.fecha = :hoy AND s.completado >= s.habito.metaDiaria", 
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
                "SELECT COUNT(s) FROM SeguimientoHabito s WHERE s.habito.usuarioId = :usuarioId AND s.fecha BETWEEN :inicio AND :fin AND s.completado >= s.habito.metaDiaria", 
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
