package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.TareaKanban;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class TareaKanbanDAO {

    public TareaKanban save(TareaKanban tarea) {
        executeWithTransaction(em ->{
           if (tarea.getId() == null){
               em.persist(tarea);
           }else {
               em.merge(tarea);
           }
        });
        return tarea;
    }

    private void executeWithTransaction(java.util.function.Consumer<EntityManager> operation) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error en la transacción del TareaKanbanDAO", e);
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        executeWithTransaction(em -> { // Usamos el mismo helper
            Optional<TareaKanban> tareaOpt = findById(id);
            if (tareaOpt.isPresent()) {
                em.remove(em.merge(tareaOpt.get()));
            }
        });
    }

    public Optional<TareaKanban> findById(Integer id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TareaKanban tarea = em.find(TareaKanban.class, id);
            return Optional.ofNullable(tarea);
        }finally {
            em.close();
        }
    }
}
