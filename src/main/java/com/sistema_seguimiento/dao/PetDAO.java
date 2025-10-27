package com.sistema_seguimiento.dao;

import com.sistema_seguimiento.model.Pet;
import com.sistema_seguimiento.model.PetType;
import com.sistema_seguimiento.model.UserPet;
import com.sistema_seguimiento.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

/**
 * DAO para gestión de Mascotas (Pet) y la relación Usuario-Mascota (UserPet)
 */
public class PetDAO {

    // ===== Operaciones sobre Pet =====

    public Pet savePet(Pet pet) {
        if (pet == null) throw new IllegalArgumentException("pet no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        EntityTransaction tx = null; // Inicializar transacción a null
        try {
            tx = em.getTransaction(); // Obtener transacción
            tx.begin(); // Iniciar
            Pet savedPet;
            if (pet.getId() == null) {
                em.persist(pet); // Guardar nuevo
                savedPet = pet;
            } else {
                savedPet = em.merge(pet); // Actualizar existente
            }
            tx.commit(); // Confirmar
            return savedPet;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback(); // Rollback en error
            throw new RuntimeException("Error al guardar Pet plantilla", e);
        } finally {
            if (em != null && em.isOpen()) em.close(); // Siempre cerrar EM
        }
    }

    public Optional<Pet> findPetById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        try {
            Pet pet = em.find(Pet.class, id); // Buscar por ID
            return Optional.ofNullable(pet); // Devolver Optional
        } finally {
            if (em != null && em.isOpen()) em.close(); // Siempre cerrar EM
        }
    }

    public Optional<Pet> findPetByType(PetType type) {
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        try {
            TypedQuery<Pet> q = em.createQuery("SELECT p FROM Pet p WHERE p.type = :t", Pet.class);
            q.setParameter("t", type);
            q.setMaxResults(1);
            return q.getResultStream().findFirst();
        } catch (Exception e) {
            System.err.println("Error buscando Pet plantilla por tipo " + type + ": " + e.getMessage());
            return Optional.empty(); // Devolver vacío en caso de error
        } finally {
            if (em != null && em.isOpen()) em.close(); // Siempre cerrar EM
        }
    }

    // Método auxiliar para buscar Pet plantilla DENTRO de una transacción existente
    private Optional<Pet> findPetByTypeWithinTx(EntityManager em, PetType type) {
        try {
            TypedQuery<Pet> q = em.createQuery("SELECT p FROM Pet p WHERE p.type = :t", Pet.class);
            q.setParameter("t", type);
            q.setMaxResults(1);
            return Optional.of(q.getSingleResult()); // Usar getSingleResult aquí está bien si se espera que exista o se cree
        } catch (NoResultException e) {
            return Optional.empty(); // Devolver vacío si no se encuentra
        }
        // No cerramos el EM aquí porque lo gestiona el método que llama
    }

    // ===== Operaciones sobre UserPet (Mascota del Usuario) =====

    public UserPet saveUserPet(UserPet userPet) {
        if (userPet == null) throw new IllegalArgumentException("userPet no puede ser null");
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            UserPet savedUserPet;
            if (userPet.getId() == null) {
                em.persist(userPet); // Guardar nuevo
                savedUserPet = userPet;
            } else {
                savedUserPet = em.merge(userPet); // Actualizar existente
            }
            tx.commit();
            return savedUserPet;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar UserPet", e);
        } finally {
            if (em != null && em.isOpen()) em.close(); // Cerrar EM
        }
    }

    /**
     * TAREA 7 - Método 1/3
     * Crea una nueva mascota activa para el usuario, desactivando la anterior si existe.
     * Si no existe la plantilla Pet para el tipo, se crea una por defecto.
     */
    public UserPet saveNewPet(Long usuarioId, PetType petType) {
        if (usuarioId == null || petType == null) throw new IllegalArgumentException("usuarioId/petType no pueden ser null");
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            // Buscar al Usuario usando el EntityManager actual
            Usuario usuario = em.find(Usuario.class, usuarioId.intValue());
            if (usuario == null) {
                if (tx.isActive()) tx.rollback();
                throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
            }

            // Desactivar la mascota activa actual si existe, usando una query de actualización para eficiencia
            int updatedCount = em.createQuery("UPDATE UserPet up SET up.active = false WHERE up.usuario.id = :uid AND up.active = true")
                    .setParameter("uid", usuarioId.intValue())
                    .executeUpdate(); // Ejecutar la actualización
            if (updatedCount > 0) {
                System.out.println("Mascota(s) anterior(es) (" + updatedCount + ") desactivada(s) para usuario ID: " + usuarioId);
            }


            // Buscar o crear plantilla Pet DENTRO de la transacción
            Pet petPlantilla = findPetByTypeWithinTx(em, petType).orElseGet(() -> {
                System.out.println("Plantilla Pet no encontrada para " + petType + ", creando una nueva.");
                Pet nuevaPlantilla = new Pet(petType, petType.name(), "Mascota " + petType.name() + " auto-generada");
                em.persist(nuevaPlantilla);
                return nuevaPlantilla;
            });

            // Crear la nueva relación UserPet
            UserPet nuevaUserPet = new UserPet(usuario, petPlantilla, petType); // Asegúrate que el constructor asigne estado/tipo correctamente
            nuevaUserPet.setActive(true);
            em.persist(nuevaUserPet); // Persistir la nueva relación DENTRO de la transacción

            tx.commit(); // Confirmar transacción SOLO si todo fue bien
            System.out.println("Nueva mascota " + petType + " creada y activada para usuario ID: " + usuarioId);
            return nuevaUserPet;

        } catch (Exception e) {
            // Error durante la transacción, hacer rollback si está activa
            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.err.println("Rollback realizado debido a error en saveNewPet.");
            }
            System.err.println("Error al ejecutar saveNewPet para usuario ID: " + usuarioId + " - " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo
            throw new RuntimeException("Error en saveNewPet", e); // Relanzar excepción
        } finally {
            // Asegurar que el EntityManager se cierre SIEMPRE
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    /**
     * TAREA 7 - Método 2/3
     * Actualiza el estado de la mascota activa del usuario.
     */
    public boolean updatePetState(Long usuarioId, PetType newState) {
        if (usuarioId == null || newState == null) throw new IllegalArgumentException("usuarioId/newState no pueden ser null");
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin(); // Iniciar transacción

            // Buscar la mascota activa actual
            TypedQuery<UserPet> query = em.createQuery(
                    "SELECT up FROM UserPet up WHERE up.usuario.id = :uid AND up.active = true", UserPet.class);
            query.setParameter("uid", usuarioId.intValue());
            query.setMaxResults(1);

            Optional<UserPet> currentPetOpt = query.getResultStream().findFirst();

            if (currentPetOpt.isPresent()) {
                UserPet currentPet = currentPetOpt.get();
                currentPet.setState(newState); // Actualizar el estado del objeto JPA
                tx.commit(); // Confirmar transacción
                System.out.println("Estado de mascota actualizado a " + newState + " para usuario ID: " + usuarioId);
                return true; // Indicar éxito
            } else {
                // No se encontró mascota activa, no hay nada que actualizar.
                System.out.println("No se encontró mascota activa para usuario ID: " + usuarioId + " para actualizar estado.");
                if (tx.isActive()) tx.rollback(); // Hacer rollback si no se hizo nada
                return false; // Indicar que no se actualizó
            }
        } catch (Exception e) {
            // Error durante la transacción, hacer rollback
            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.err.println("Rollback realizado debido a error en updatePetState.");
            }
            System.err.println("Error al actualizar estado de mascota para usuario ID: " + usuarioId + " - " + e.getMessage());
            e.printStackTrace();
            return false; // Indicar fallo
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * TAREA 7 - Método 3/3
     * Obtiene la mascota activa más reciente del usuario.
     */
    public Optional<UserPet> getCurrentUserPet(Long usuarioId) {
        if (usuarioId == null) {
            // Puedes decidir lanzar una excepción o simplemente devolver Optional.empty()
            System.err.println("getCurrentUserPet llamado con usuarioId null.");
            return Optional.empty();
        }
        EntityManager em = EntityManagerUtil.getEntityManager(); // Obtener EM
        try {
            // Query para encontrar la UserPet activa, ordenada por fecha de desbloqueo descendente para obtener la más reciente si hubiera varias (aunque la lógica de saveNewPet debería prevenirlo)
            TypedQuery<UserPet> query = em.createQuery(
                    "SELECT up FROM UserPet up JOIN FETCH up.pet WHERE up.usuario.id = :uid AND up.active = true ORDER BY up.unlockedAt DESC",
                    UserPet.class);
            query.setParameter("uid", usuarioId.intValue());
            query.setMaxResults(1); // Solo necesitamos la más reciente activa

            return query.getResultStream().findFirst();

        } catch (Exception e) {
            System.err.println("Error al buscar mascota activa para usuario ID " + usuarioId + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty(); // Devolver Optional vacío en caso de error
        } finally {
            // Asegurar que el EntityManager se cierre SIEMPRE
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}