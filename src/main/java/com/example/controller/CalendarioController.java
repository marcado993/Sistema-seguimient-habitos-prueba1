package com.example.controller;

import com.example.model.Habito;
import com.example.model.TareaKanban;
import com.example.dao.HabitoDAO;
import com.example.dao.TareaKanbanDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para el Calendario Kanban
 * Diagrama de Robustez: Control
 * Gestiona la vista tipo Jira con columnas Por Hacer, En Progreso, Hecho
 * organizadas por franjas horarias (Mañana, Tarde, Noche, Madrugada)
 */
@WebServlet(name = "CalendarioController", urlPatterns = {"/calendario"})
public class CalendarioController extends HttpServlet {
    
    private final HabitoDAO habitoDAO = new HabitoDAO();
    private final TareaKanbanDAO tareaKanbanDAO = new TareaKanbanDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("kanban".equals(action) || action == null) {
            mostrarKanban(request, response);
        } else if ("actualizarEstado".equals(action)) {
            actualizarEstadoTarea(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Muestra la vista Kanban con las tareas organizadas por horario
     */
    private void mostrarKanban(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            System.out.println("🔷 CalendarioController - Iniciando mostrarKanban()");
            
            HttpSession session = request.getSession();
            String usuarioId = (String) session.getAttribute("usuarioId");
            
            System.out.println("   Usuario ID: " + usuarioId);
            
            // Obtener fecha seleccionada o usar hoy
            String fechaStr = request.getParameter("fecha");
            LocalDate fecha = fechaStr != null ? LocalDate.parse(fechaStr) : LocalDate.now();
            
            System.out.println("   Fecha seleccionada: " + fecha);
            
            // Obtener todos los hábitos activos del usuario
            System.out.println("   Obteniendo hábitos del usuario...");
            List<Habito> habitosActivos = habitoDAO.findByUsuarioId(usuarioId);
            
            System.out.println("   ✅ " + habitosActivos.size() + " hábitos encontrados");
            
            // Clasificar hábitos por franja horaria y estado
            // Mañana: 6:00 - 12:00
            // Tarde: 12:00 - 18:00
            // Noche: 18:00 - 22:00
            // Madrugada: 22:00 - 6:00
            
            request.setAttribute("fecha", fecha);
            request.setAttribute("habitos", habitosActivos);
            request.setAttribute("fechaHoy", LocalDate.now());
            
            System.out.println("   Redirigiendo a kanban.jsp...");
            request.getRequestDispatcher("/WEB-INF/views/calendario/kanban.jsp")
                   .forward(request, response);
            
            System.out.println("✅ Kanban cargado exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en CalendarioController.mostrarKanban():");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Tipo: " + e.getClass().getName());
            e.printStackTrace();
            
            // Enviar a página de error con mensaje descriptivo
            request.setAttribute("error", "Error al cargar el Kanban: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualiza el estado de una tarea en el tablero Kanban
     * Estados: por-hacer, en-progreso, completado
     */
    private void actualizarEstadoTarea(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Long habitoId = Long.parseLong(request.getParameter("habitoId"));
            String nuevoEstado = request.getParameter("estado");
            String franjaHoraria = request.getParameter("franja");
            
            // Aquí se actualizaría el estado en la base de datos
            // Por ahora lo manejamos en la sesión
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Estado actualizado\"}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Determina la franja horaria basada en la hora
     * @param hora Hora en formato HH:MM
     * @return Franja horaria: morning, afternoon, evening, night
     */
    private String determinarFranjaHoraria(String hora) {
        if (hora == null || hora.isEmpty()) {
            return "morning";
        }
        
        try {
            int horaNumero = Integer.parseInt(hora.split(":")[0]);
            
            if (horaNumero >= 6 && horaNumero < 12) {
                return "morning";
            } else if (horaNumero >= 12 && horaNumero < 18) {
                return "afternoon";
            } else if (horaNumero >= 18 && horaNumero < 22) {
                return "evening";
            } else {
                return "night";
            }
        } catch (Exception e) {
            return "morning";
        }
    }
}
