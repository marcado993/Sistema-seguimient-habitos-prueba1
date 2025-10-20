package com.sistema_seguimiento.dao;

import jakarta.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Clase base abstracta para todos los DAOs
 * 
 * Proporciona métodos utilitarios para centralizar la gestión de transacciones
 * y consultas, eliminando código repetitivo en los DAOs.
 * 
 * Patrón aplicado: Template Method + Strategy (con lambdas)
 */
public abstract class BaseDAO {
    
    /**
     * Ejecuta una operación con transacción (sin retorno).
     * 
     * Encapsula el patrón completo de transacción:
     * 1. Obtener EntityManager
     * 2. Iniciar transacción (begin)
     * 3. Ejecutar operación
     * 4. Confirmar transacción (commit)
     * 5. Manejo de errores con rollback
     * 6. Cerrar EntityManager (finally)
     * 
     * @param operation La operación a ejecutar con el EntityManager
     * @param errorMessage El mensaje de error en caso de excepción
     * 
     * Ejemplo de uso:
     * <pre>
     * executeWithTransaction(
     *     em -> em.persist(objetivo),
     *     "Error al guardar objetivo"
     * );
     * </pre>
     */
    protected void executeWithTransaction(Consumer<EntityManager> operation, String errorMessage) {
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
     * Ejecuta una operación con transacción y retorno.
     * 
     * Similar a executeWithTransaction pero permite devolver un valor.
     * Útil para operaciones de creación/actualización que retornan la entidad.
     * 
     * @param <T> El tipo de retorno de la operación
     * @param operation La función que ejecuta la operación con el EntityManager
     * @param errorMessage El mensaje de error en caso de excepción
     * @return El resultado de la operación
     * 
     * Ejemplo de uso:
     * <pre>
     * Objetivo objetivo = executeWithTransactionAndReturn(
     *     em -> {
     *         em.persist(nuevoObjetivo);
     *         return nuevoObjetivo;
     *     },
     *     "Error al guardar objetivo"
     * );
     * </pre>
     */
    protected <T> T executeWithTransactionAndReturn(Function<EntityManager, T> operation, String errorMessage) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T result = operation.apply(em);
            em.getTransaction().commit();
            return result;
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
     * 
     * Encapsula el patrón de try-finally-close para consultas de solo lectura.
     * Las consultas no requieren transacción explícita.
     * 
     * @param <T> El tipo de retorno de la consulta
     * @param query La función que ejecuta la consulta
     * @return El resultado de la consulta
     * 
     * Ejemplo de uso:
     * <pre>
     * List<Objetivo> objetivos = executeQuery(em -> 
     *     em.createQuery("FROM Objetivo WHERE usuarioId = :userId", Objetivo.class)
     *       .setParameter("userId", userId)
     *       .getResultList()
     * );
     * </pre>
     */
    protected <T> T executeQuery(Function<EntityManager, T> query) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return query.apply(em);
        } finally {
            em.close();
        }
    }
}
