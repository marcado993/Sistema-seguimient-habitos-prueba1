package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.RegistroProgreso;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO para gestión de Objetivos
 * 
 * Refactorizado para usar BaseDAO y eliminar código repetitivo
 * de gestión de transacciones.
 */
public class ObjetivoDAO extends BaseDAO {

    public List<Objetivo> findByUsuarioId(String usuarioId) {
        return executeQuery(em ->
            em.createQuery(
                "FROM Objetivo WHERE usuarioId = ?1 ORDER BY fechaCreacion DESC", 
                Objetivo.class)
                .setParameter(1, usuarioId)
                .getResultList()
        );
    }

    public Optional<Objetivo> findById(Long id) {
        return executeQuery(em -> {
            Objetivo objetivo = em.find(Objetivo.class, id);
            return Optional.ofNullable(objetivo);
        });
    }

    public List<Objetivo> findObjetivosActivos(String usuarioId) {
        return executeQuery(em -> {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'ACTIVO' ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        });
    }

    public List<Objetivo> findObjetivosCompletados(String usuarioId) {
        return executeQuery(em -> {
            TypedQuery<Objetivo> query = em.createQuery(
                "SELECT o FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'COMPLETADO' ORDER BY o.fechaCreacion DESC", 
                Objetivo.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        });
    }

    public Objetivo save(Objetivo objetivo) {
        return executeWithTransactionAndReturn(em -> {
            // Log para debugging
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
            
            System.out.println("✅ Transacción confirmada");
            return result;
        }, "Error al guardar objetivo");
    }

    public void delete(Long id) {
        executeWithTransaction(em -> {
            Objetivo objetivo = em.find(Objetivo.class, id);
            if (objetivo != null) {
                em.remove(objetivo);
            }
        }, "Error al eliminar objetivo");
    }

    public void actualizarProgreso(Long objetivoId, int nuevoProgreso, String observaciones) {
        executeWithTransaction(em -> {
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
        }, "Error al actualizar progreso");
    }

    public List<RegistroProgreso> findRegistrosProgreso(Long objetivoId) {
        return executeQuery(em -> {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.objetivo.id = :objetivoId ORDER BY r.fechaRegistro DESC", 
                RegistroProgreso.class);
            query.setParameter("objetivoId", objetivoId);
            return query.getResultList();
        });
    }

    public Long countObjetivosCompletados(String usuarioId) {
        return executeQuery(em -> {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(o) FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'COMPLETADO'", 
                Long.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getSingleResult();
        });
    }

    public Double getProgresoPromedio(String usuarioId) {
        return executeQuery(em -> {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(o.progreso) FROM Objetivo o WHERE o.usuarioId = :usuarioId AND o.estado = 'ACTIVO'", 
                Double.class);
            query.setParameter("usuarioId", usuarioId);
            Double resultado = query.getSingleResult();
            return resultado != null ? resultado : 0.0;
        });
    }
}

