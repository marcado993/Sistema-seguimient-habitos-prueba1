package com.sistema_seguimiento.services;

import com.sistema_seguimiento.model.PetType;
import org.junit.Assert;
import org.junit.Test;

public class PetUnlockServiceTest {

    @Test
    public void dadoTotalHabitos5_entoncesNoHayDesbloqueo_retornaNull() {
        // Arrange
        PetUnlockService service = new PetUnlockService();
        Long usuarioId = 1L;
        int totalHabits = 5;

        // Act
        PetType resultado = service.checkEvolution(usuarioId, totalHabits);

        // Assert
        Assert.assertNull("Con 5 hábitos no debería haber desbloqueo/evolución (null)", resultado);
    }

    @Test
    public void dadoTotalHabitos10_entoncesDesbloqueaHuevo_retornaHuevo() {
        // Arrange
        PetUnlockService service = new PetUnlockService();
        Long usuarioId = 1L;
        int totalHabits = 10;

        // Act
        PetType resultado = service.checkEvolution(usuarioId, totalHabits);

        // Assert (fallará mientras checkEvolution retorne null)
        Assert.assertEquals("Con 10 hábitos debe desbloquear HUEVO", PetType.HUEVO, resultado);
    }
}
