package com.example.servlet;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.RegistroProgreso;
import com.example.model.RegistroProgresoDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de Hábitos según el diagrama de clases
 * Maneja las operaciones relacionadas con hábitos
 */
@WebServlet("/controlador-habitos")
public class ControladorHabitos extends HttpServlet {
    
    private HabitoDAO habitoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoDAO = new HabitoDAO();
    }
    
    /**
     * Registrar cumplimiento de un hábito
     * Basado en el diagrama de secuencia
     */
    public Habito registrarCumplimiento(Long habitoId, LocalDate fecha, String estado) {
        try {
            Habito habito = habitoDAO.findById(habitoId).orElse(null);
            
            if (habito == null) {
                return null;
            }
            
            // Buscar o crear seguimiento para la fecha
            // Actualizar estado de cumplimiento
            
            return habito;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtener progreso de un hábito
     */
    public List<RegistroProgresoDTO> obtenerProgreso(Long habitoId, List<RegistroProgreso> registros) {
        // Lógica para obtener y transformar el progreso
        return null;
    }
    
    /**
     * Buscar hábito por ID
     */
    public Habito buscarHabito(Long habitoId) {
        return habitoDAO.findById(habitoId).orElse(null);
    }
    
    /**
     * Crear ficha de racha actual
     */
    public int crearFichaRacha(Habito habito, String estado, boolean cumplido) {
        if (habito != null) {
            return habito.calcularRachaActual();
        }
        return 0;
    }
    
    /**
     * Devolver nuevo registro
     */
    public RegistroProgreso devolverNuevoRegistro(Habito habito) {
        // Crear nuevo registro de progreso
        return new RegistroProgreso();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("registrar".equals(action)) {
            // Lógica para registrar cumplimiento
            String habitoIdStr = request.getParameter("habitoId");
            if (habitoIdStr != null) {
                Long habitoId = Long.parseLong(habitoIdStr);
                Habito habito = registrarCumplimiento(habitoId, LocalDate.now(), "COMPLETADO");
                
                if (habito != null) {
                    response.sendRedirect("habitos?action=list&success=true");
                } else {
                    response.sendRedirect("habitos?action=list&error=true");
                }
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
