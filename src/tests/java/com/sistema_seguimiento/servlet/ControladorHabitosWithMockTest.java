package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.dao.HabitoDAO;
import com.sistema_seguimiento.model.Habito;
import com.sistema_seguimiento.services.PointsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * � FASE VERDE - Prueba con Mock (2/2) para ControladorHabitos
 * 
 * Verifica que ControladorHabitos llame a PointsService.addPointsToUser()
 * después de un registro exitoso de hábito
 */
public class ControladorHabitosWithMockTest {
    
    private ControladorHabitos controlador;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private HabitoDAO habitoDAO;
    private PointsService pointsService;
    
    @BeforeEach
    void setup() {
        // Mocks
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        habitoDAO = mock(HabitoDAO.class);
        pointsService = mock(PointsService.class);
        
        // Configurar mocks básicos
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        
        // Crear controlador
        controlador = new ControladorHabitos();
    }
    
    /**
     * � UNA SOLA PRUEBA CON MOCK (2/2) - TDD Puro
     * Verifica que después de un registro exitoso se llame a PointsService
     */
    @Test
    @DisplayName("doPost con registro CUMPLIDO debe llamar a PointsService.addPointsToUser una vez")
    void testDoPost_RegistroCumplido_DebeLlamarAddPointsToUser() throws Exception {
        // Arrange
        Integer usuarioId = 1;
        Integer habitoId = 100;
        String estado = "CUMPLIDO";
        
        // Mock del Usuario en sesión
        com.sistema_seguimiento.model.Usuario usuarioMock = new com.sistema_seguimiento.model.Usuario();
        usuarioMock.setId(usuarioId);
        usuarioMock.setNombre("Test User");
        
        when(request.getParameter("action")).thenReturn("registrar");
        when(request.getParameter("habitoId")).thenReturn(habitoId.toString());
        when(request.getParameter("estado")).thenReturn(estado);
        when(request.getParameter("notas")).thenReturn("Test notas");
        when(request.getParameter("fecha")).thenReturn(LocalDate.now().toString());
        when(session.getAttribute("usuario")).thenReturn(usuarioMock); // FIX: "usuario" no "usuarioId"
        
        // Mock del Habito
        Habito habitoMock = new Habito();
        habitoMock.setId(habitoId);
        habitoMock.setNombre("Hábito Test");
        habitoMock.setUsuarioId(usuarioId);
        
        // Mock del RegistroHabito guardado exitosamente
        com.sistema_seguimiento.model.RegistroHabito registroGuardado = 
            new com.sistema_seguimiento.model.RegistroHabito();
        registroGuardado.setId(999);
        registroGuardado.setHabito(habitoMock);
        registroGuardado.setFecha(LocalDate.now());
        registroGuardado.setCompletado(true);
        
        // Mocks del HabitoDAO
        when(habitoDAO.findById(habitoId)).thenReturn(java.util.Optional.of(habitoMock));
        when(habitoDAO.saveRegistro(any())).thenReturn(registroGuardado);
        
        // Inyectar mocks en el controlador
        controlador.setHabitoDAO(habitoDAO);
        controlador.setPointsService(pointsService);
        
        // Act
        controlador.doPost(request, response);
        
        // Assert
        // Verificar que addPointsToUser fue llamado EXACTAMENTE 1 vez
        verify(pointsService, times(1)).addPointsToUser(usuarioId, estado);
    }
}
