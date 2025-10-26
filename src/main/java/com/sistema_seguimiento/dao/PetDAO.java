package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Pet;
import com.sistema_seguimiento.model.PetType;
import com.sistema_seguimiento.model.UserPet;
import com.sistema_seguimiento.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

/**
 * DAO para gestión de Mascotas (Pet) y la relación Usuario-Mascota (UserPet)
 */
public class PetDAO {

    // ===== Operaciones sobre Pet =====

    public Pet savePet(Pet pet) {
        if (pet == null) throw new IllegalArgumentException("pet no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (pet.getId() == null) {
                em.persist(pet);
            } else {
                pet = em.merge(pet);
            }
            tx.commit();
            return pet;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar Pet", e);
        } finally {
            em.close();
        }
    }

    public Optional<Pet> findPetById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Pet.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Pet> findPetByType(PetType type) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Pet> q = em.createQuery("SELECT p FROM Pet p WHERE p.type = :t", Pet.class);
            q.setParameter("t", type);
            q.setMaxResults(1);
            return q.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    // ===== Operaciones sobre UserPet =====

    public UserPet saveUserPet(UserPet userPet) {
        if (userPet == null) throw new IllegalArgumentException("userPet no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (userPet.getId() == null) {
                em.persist(userPet);
            } else {
                userPet = em.merge(userPet);
            }
            tx.commit();
            return userPet;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar UserPet", e);
        } finally {
            em.close();
        }
    }

    public UserPet updateUserPet(UserPet userPet) {
        if (userPet == null) throw new IllegalArgumentException("userPet no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            UserPet merged = em.merge(userPet);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al actualizar UserPet", e);
        } finally {
            em.close();
        }
    }

    public Optional<UserPet> getCurrentUserPet(Long usuarioId) {
        if (usuarioId == null) throw new IllegalArgumentException("usuarioId no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<UserPet> q = em.createQuery(
                    "SELECT up FROM UserPet up WHERE up.usuario.id = :uid AND up.active = true ORDER BY up.unlockedAt DESC", UserPet.class);
            q.setParameter("uid", usuarioId.intValue());
            q.setMaxResults(1);
            return q.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    /**
     * Crea una nueva mascota activa para el usuario, desactivando la anterior si existe.
     * Si no existe la plantilla Pet para el tipo, se crea una por defecto.
     */
    public UserPet saveNewPet(Long usuarioId, PetType petType) {
        if (usuarioId == null || petType == null) throw new IllegalArgumentException("usuarioId/petType no pueden ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Usuario usuario = em.find(Usuario.class, usuarioId.intValue());
            if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado: " + usuarioId);

            // Desactivar la actual si la hay
            TypedQuery<UserPet> q = em.createQuery(
                    "SELECT up FROM UserPet up WHERE up.usuario.id = :uid AND up.active = true", UserPet.class);
            q.setParameter("uid", usuarioId.intValue());
            q.getResultList().forEach(up -> up.setActive(false));

            // Buscar o crear plantilla Pet
            Pet pet = findPetByTypeWithinTx(em, petType).orElseGet(() -> {
                Pet p = new Pet(petType, petType.name(), "Auto-creada");
                em.persist(p);
                return p;
            });

            UserPet nuevo = new UserPet(usuario, pet, petType);
            nuevo.setActive(true);
            em.persist(nuevo);

            tx.commit();
            return nuevo;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al crear nueva UserPet", e);
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza el estado de la mascota activa del usuario.
     */
    public boolean updatePetState(Long usuarioId, PetType newState) {
        if (usuarioId == null || newState == null) throw new IllegalArgumentException("usuarioId/newState no pueden ser null");
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TypedQuery<UserPet> q = em.createQuery(
                    "SELECT up FROM UserPet up WHERE up.usuario.id = :uid AND up.active = true", UserPet.class);
            q.setParameter("uid", usuarioId.intValue());
            q.setMaxResults(1);
            Optional<UserPet> actual = q.getResultList().stream().findFirst();
            if (actual.isEmpty()) {
                tx.rollback();
                return false;
            }
            UserPet up = actual.get();
            up.setState(newState);
            em.merge(up);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al actualizar estado de UserPet", e);
        } finally {
            em.close();
        }
    }

    // Helper que reutiliza el mismo EntityManager de la transacción
    private Optional<Pet> findPetByTypeWithinTx(EntityManager em, PetType type) {
        TypedQuery<Pet> q = em.createQuery("SELECT p FROM Pet p WHERE p.type = :t", Pet.class);
        q.setParameter("t", type);
        q.setMaxResults(1);
        return q.getResultList().stream().findFirst();
    }
}

