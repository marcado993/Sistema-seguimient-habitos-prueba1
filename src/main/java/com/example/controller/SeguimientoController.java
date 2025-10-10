package com.example.controller;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.SeguimientoHabito;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SeguimientoController", urlPatterns = {"/seguimiento"})
public class SeguimientoController extends HttpServlet {
    
    private HabitoDAO habitoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoDAO = new HabitoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            switch (action != null ? action : "view") {
                case "view":
                    mostrarSeguimiento(request, response);
                    break;
                case "calendario":
                    mostrarCalendario(request, response, usuarioId);
                    break;
                case "estadisticas":
                    mostrarEstadisticas(request, response, usuarioId);
                    break;
                default:
                    mostrarSeguimiento(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            switch (action != null ? action : "") {
                case "marcar":
                    marcarHabito(request, response);
                    break;
                case "actualizar":
                    actualizarSeguimiento(request, response);
                    break;
                default:
                    response.sendRedirect("seguimiento?action=view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void mostrarSeguimiento(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long habitoId = Long.parseLong(request.getParameter("habitoId"));
        Optional<Habito> habitoOpt = habitoDAO.findById(habitoId);
        
        if (habitoOpt.isPresent()) {
            Habito habito = habitoOpt.get();
            LocalDate fecha = LocalDate.now();
            
            // Obtener seguimiento del día actual
            SeguimientoHabito seguimientoHoy = habitoDAO.findSeguimientoByFecha(habitoId, fecha);
            
            // Obtener seguimientos de la última semana
            LocalDate fechaInicio = fecha.minusDays(7);
            
            request.setAttribute("habito", habito);
            request.setAttribute("seguimientoHoy", seguimientoHoy);
            request.setAttribute("fechaActual", fecha);
            
            request.getRequestDispatcher("/WEB-INF/views/seguimiento/detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect("habitos?action=list&error=Hábito no encontrado");
        }
    }
    
    private void mostrarCalendario(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        List<Habito> habitos = habitoDAO.findHabitosActivos(usuarioId);
        LocalDate fechaActual = LocalDate.now();
        
        // Obtener mes y año de los parámetros, o usar actual
        String mesParam = request.getParameter("mes");
        String anioParam = request.getParameter("anio");
        
        int mes = mesParam != null ? Integer.parseInt(mesParam) : fechaActual.getMonthValue();
        int anio = anioParam != null ? Integer.parseInt(anioParam) : fechaActual.getYear();
        
        LocalDate fechaMes = LocalDate.of(anio, mes, 1);
        
        request.setAttribute("habitos", habitos);
        request.setAttribute("fechaMes", fechaMes);
        request.setAttribute("fechaActual", fechaActual);
        
        request.getRequestDispatcher("/WEB-INF/views/seguimiento/calendario.jsp").forward(request, response);
    }
    
    private void mostrarEstadisticas(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        List<Habito> habitos = habitoDAO.findHabitosActivos(usuarioId);
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioMes = fechaActual.withDayOfMonth(1);
        
        request.setAttribute("habitos", habitos);
        request.setAttribute("fechaActual", fechaActual);
        request.setAttribute("fechaInicioMes", fechaInicioMes);
        
        request.getRequestDispatcher("/WEB-INF/views/seguimiento/estadisticas.jsp").forward(request, response);
    }
    
    private void marcarHabito(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long habitoId = Long.parseLong(request.getParameter("habitoId"));
        String notasParam = request.getParameter("notas");
        String fechaParam = request.getParameter("fecha");
        
        LocalDate fecha = fechaParam != null ? LocalDate.parse(fechaParam) : LocalDate.now();
        String notas = notasParam != null ? notasParam : "";
        
        habitoDAO.registrarCompletado(habitoId, fecha, notas);
        
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl == null || redirectUrl.isEmpty()) {
            redirectUrl = "seguimiento?action=view&habitoId=" + habitoId;
        }
        
        response.sendRedirect(redirectUrl + "&success=Hábito marcado exitosamente");
    }
    
    private void actualizarSeguimiento(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long habitoId = Long.parseLong(request.getParameter("habitoId"));
        String fechaParam = request.getParameter("fecha");
        String completadoParam = request.getParameter("completado");
        String notas = request.getParameter("notas");
        
        LocalDate fecha = LocalDate.parse(fechaParam);
        Integer completado = Integer.parseInt(completadoParam);
        
        // Buscar seguimiento existente
        SeguimientoHabito seguimiento = habitoDAO.findSeguimientoByFecha(habitoId, fecha);
        
        if (seguimiento != null) {
            seguimiento.setCompletado(completado);
            seguimiento.setNotas(notas);
            habitoDAO.saveSeguimiento(seguimiento);
        } else {
            // Crear nuevo seguimiento
            Optional<Habito> habitoOpt = habitoDAO.findById(habitoId);
            if (habitoOpt.isPresent()) {
                seguimiento = new SeguimientoHabito(habitoOpt.get(), fecha, completado, notas);
                habitoDAO.saveSeguimiento(seguimiento);
            }
        }
        
        response.sendRedirect("seguimiento?action=view&habitoId=" + habitoId + "&success=Seguimiento actualizado");
    }
}
