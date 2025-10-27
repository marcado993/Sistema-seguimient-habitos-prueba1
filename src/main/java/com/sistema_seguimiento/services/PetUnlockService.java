package com.sistema_seguimiento.services;

import com.sistema_seguimiento.model.PetType;

/**
 * Servicio responsable de decidir si hay desbloqueo/evolución de mascota
 * a partir del total de hábitos cumplidos del usuario.
 */
public class PetUnlockService {

    /**
     * Determina si el usuario desbloquea/evoluciona una mascota según su total de hábitos.
     * Reglas:
     *  - 50 o más hábitos => BEBE
     *  - 10 o más hábitos => HUEVO
     *  - En otro caso => null
     *
     * @param usuarioId   id del usuario (reservado para futuras reglas por usuario)
     * @param totalHabits total de hábitos cumplidos
     * @return PetType del hito alcanzado, o null si no hay cambios
     */
    public PetType checkEvolution(Long usuarioId, int totalHabits) {
        // Tarea 5: agregar evolución a 50 hábitos y mantener el hito de 10
        if (totalHabits >= 50) {
            return PetType.BEBE;
        } else if (totalHabits >= 10) {
            // Tarea 3: al alcanzar 10 hábitos se desbloquea HUEVO
            return PetType.HUEVO;
        }
        return null;
    }
}