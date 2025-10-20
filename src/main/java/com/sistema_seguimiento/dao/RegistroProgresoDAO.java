package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.RegistroProgreso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RegistroProgresoDAO {
    
    /**
     * Ejecuta una operación con transacción (sin retorno).
     * Encapsula el patrón de begin/commit/rollback/close para evitar duplicación.
     * 
     * @param operation La operación a ejecutar con el EntityManager
     * @param errorMessage El mensaje de error en caso de excepción
     */
    private void executeWithTransaction(Consumer<EntityManager> operation, String errorMessage) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            operation.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(errorMessage, e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Ejecuta una consulta (sin transacción) con retorno.
     * Encapsula el patrón de try-finally-close para consultas.
     * 
     * @param <T> El tipo de retorno de la consulta
     * @param query La función que ejecuta la consulta
     * @return El resultado de la consulta
     */
    private <T> T executeQuery(Function<EntityManager, T> query) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return query.apply(em);
        } finally {
            em.close();
        }
    }
    
    /**
     * Guarda un nuevo registro de progreso
     */
    public void guardar(RegistroProgreso registro) {
        executeWithTransaction(
            em -> em.persist(registro),
            "Error al guardar registro de progreso"
        );
    }
    
    /**
     * Actualiza un registro de progreso existente
     */
    public void actualizar(RegistroProgreso registro) {
        executeWithTransaction(
            em -> em.merge(registro),
            "Error al actualizar registro de progreso"
        );
    }
    
    /**
     * Obtiene todos los registros de progreso de un hábito
     */
    public List<RegistroProgreso> obtenerPorHabito(Long habitoId) {
        return executeQuery(em -> {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r WHERE r.habito.id = :habitoId ORDER BY r.fecha DESC",
                RegistroProgreso.class
            );
            query.setParameter("habitoId", habitoId);
            return query.getResultList();
        });
    }
    
    /**
     * Obtiene un registro de progreso por ID
     */
    public RegistroProgreso obtenerPorId(Long id) {
        return executeQuery(em -> em.find(RegistroProgreso.class, id));
    }
    
    /**
     * Obtiene todos los registros de progreso
     */
    public List<RegistroProgreso> obtenerTodos() {
        return executeQuery(em -> {
            TypedQuery<RegistroProgreso> query = em.createQuery(
                "SELECT r FROM RegistroProgreso r ORDER BY r.fecha DESC",
                RegistroProgreso.class
            );
            return query.getResultList();
        });
    }
    
    /**
     * Elimina un registro de progreso
     */
    public void eliminar(Long id) {
        executeWithTransaction(
            em -> {
                RegistroProgreso registro = em.find(RegistroProgreso.class, id);
                if (registro != null) {
                    em.remove(registro);
                }
            },
            "Error al eliminar registro de progreso"
        );
    }
    
    /**
     * Cuenta los registros cumplidos de un hábito
     */
    public long contarCumplidos(Long habitoId) {
        return executeQuery(em -> {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM RegistroProgreso r WHERE r.habito.id = :habitoId AND r.cumplido = true",
                Long.class
            );
            query.setParameter("habitoId", habitoId);
            return query.getSingleResult();
        });
    }
}
