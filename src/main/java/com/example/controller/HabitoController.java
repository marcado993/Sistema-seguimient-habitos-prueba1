package com.example.controller;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
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

@WebServlet(name = "HabitoController", urlPatterns = {"/habitos", "/habito"})
public class HabitoController extends HttpServlet {
    
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
            // Si no hay sesión, crear una sesión demo temporal
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
            usuarioId = "demo";
        }
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listarHabitos(request, response, usuarioId);
                    break;
                case "new":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "edit":
                    mostrarFormularioEditar(request, response);
                    break;
                case "delete":
                    eliminarHabito(request, response);
                    break;
                case "view":
                    verDetalleHabito(request, response);
                    break;
                case "dashboard":
                    mostrarDashboard(request, response, usuarioId);
                    break;
                default:
                    listarHabitos(request, response, usuarioId);
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
            // Si no hay sesión, crear una sesión demo temporal
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
            usuarioId = "demo";
        }
        
        try {
            switch (action != null ? action : "") {
                case "create":
                    crearHabito(request, response, usuarioId);
                    break;
                case "update":
                    actualizarHabito(request, response);
                    break;
                case "marcar":
                    marcarHabitoCompletado(request, response);
                    break;
                default:
                    response.sendRedirect("habitos?action=list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void listarHabitos(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        List<Habito> habitos = habitoDAO.findByUsuarioId(usuarioId);
        request.setAttribute("habitos", habitos);
        request.getRequestDispatcher("/WEB-INF/views/habitos/lista.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/habitos/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Habito> habitoOpt = habitoDAO.findById(id);
        
        if (habitoOpt.isPresent()) {
            request.setAttribute("habito", habitoOpt.get());
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/WEB-INF/views/habitos/formulario.jsp").forward(request, response);
        } else {
            response.sendRedirect("habitos?action=list&error=Hábito no encontrado");
        }
    }
    
    private void crearHabito(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String frecuenciaStr = request.getParameter("frecuencia");
        String metaDiariaStr = request.getParameter("metaDiaria");
        
        Habito.FrecuenciaHabito frecuencia = Habito.FrecuenciaHabito.valueOf(frecuenciaStr);
        Integer metaDiaria = Integer.parseInt(metaDiariaStr);
        
        Habito habito = new Habito(nombre, descripcion, frecuencia, metaDiaria, usuarioId);
        habitoDAO.save(habito);
        
        response.sendRedirect("habitos?action=list&success=Hábito creado exitosamente");
    }
    
    private void actualizarHabito(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String frecuenciaStr = request.getParameter("frecuencia");
        String metaDiariaStr = request.getParameter("metaDiaria");
        
        Optional<Habito> habitoOpt = habitoDAO.findById(id);
        if (habitoOpt.isPresent()) {
            Habito habito = habitoOpt.get();
            habito.setNombre(nombre);
            habito.setDescripcion(descripcion);
            habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr));
            habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
            
            habitoDAO.save(habito);
            response.sendRedirect("habitos?action=list&success=Hábito actualizado exitosamente");
        } else {
            response.sendRedirect("habitos?action=list&error=Hábito no encontrado");
        }
    }
    
    private void eliminarHabito(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        habitoDAO.delete(id);
        response.sendRedirect("habitos?action=list&success=Hábito eliminado exitosamente");
    }
    
    private void verDetalleHabito(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Habito> habitoOpt = habitoDAO.findById(id);
        
        if (habitoOpt.isPresent()) {
            request.setAttribute("habito", habitoOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/habitos/detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect("habitos?action=list&error=Hábito no encontrado");
        }
    }
    
    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        List<Habito> habitos = habitoDAO.findByUsuarioId(usuarioId);
        
        // Calcular estadísticas simples
        long habitosActivos = habitos.stream().filter(h -> h.getActivo()).count();
        long habitosCompletadosHoy = 0; // Por simplicidad, ponemos 0 por ahora
        
        request.setAttribute("habitos", habitos);
        request.setAttribute("habitosActivos", habitosActivos);
        request.setAttribute("habitosCompletadosHoy", habitosCompletadosHoy);
        request.setAttribute("fechaHoy", java.time.LocalDate.now().toString());
        
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
    
    private void marcarHabitoCompletado(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Long habitoId = Long.parseLong(request.getParameter("habitoId"));
        String notas = request.getParameter("notas");
        LocalDate fecha = LocalDate.now();
        
        // Registrar el hábito como completado
        habitoDAO.registrarCompletado(habitoId, fecha, notas);
        response.sendRedirect("habitos?action=dashboard&success=Hábito marcado como completado");
    }
}
