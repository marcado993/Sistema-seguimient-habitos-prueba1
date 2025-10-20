package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.services.KanbanServicio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.Mockito;

public class ControladorKanbanTest {

    @Test
    public void cuandoSeRecibeAccionMover_entoncesSeLlamaAlMetodoMoverTareaDelServicioKanban() throws Exception {
        // Arrange
        ControladorKanban servlet = new ControladorKanban();
        KanbanServicio servicioMock = Mockito.mock(KanbanServicio.class);
        servlet.setKanbanServicio(servicioMock);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        Mockito.when(req.getParameter("accion")).thenReturn("mover");
        Mockito.when(req.getParameter("tareaId")).thenReturn("T-42");
        Mockito.when(req.getParameter("columnaDestino")).thenReturn("EN_PROGRESO");

        // Act
        servlet.doPost(req, resp);

        // Assert
        Mockito.verify(servicioMock, Mockito.times(1)).moverTarea("T-42", "EN_PROGRESO");
    }
}
