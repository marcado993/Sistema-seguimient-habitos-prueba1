package com.sistema_seguimiento.services;

import com.sistema_seguimiento.model.PetType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PetUnlockServiceParamTest {

    @Parameterized.Parameters(name = "totalHabits={0} => expected={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {9, null},
                {10, PetType.HUEVO},
                {49, PetType.HUEVO},
                {50, PetType.BEBE}
        });
    }

    @Parameterized.Parameter(0)
    public int totalHabits;

    @Parameterized.Parameter(1)
    public PetType expected;

    @Test
    public void deberiaEvaluarHitosDeMascota_SegunTotalDeHabitos() {
        // Arrange
        PetUnlockService service = new PetUnlockService();
        Long usuarioId = 1L;

        // Act
        PetType resultado = service.checkEvolution(usuarioId, totalHabits);

        // Assert
        Assert.assertEquals("Resultado incorrecto para totalHabits=" + totalHabits, expected, resultado);
    }
}

