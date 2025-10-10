package com.example.controller;

import com.example.dao.ObjetivoDAO;
import com.example.model.Objetivo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ObjetivoController", urlPatterns = {"/objetivos", "/objetivos/*"})
public class ObjetivoController extends HttpServlet {
    
    private ObjetivoDAO objetivoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.objetivoDAO = new ObjetivoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                if ("new".equals(action)) {
                    mostrarFormularioCrear(request, response);
                } else if ("edit".equals(action)) {
                    mostrarFormularioEditar(request, response);
                } else if ("view".equals(action)) {
                    verObjetivo(request, response);
                } else if ("progress".equals(action)) {
                    mostrarProgreso(request, response);
                } else {
                    listarObjetivos(request, response);
                }
            } else if (pathInfo.startsWith("/delete/")) {
                eliminarObjetivo(request, response);
            }
        } catch (Exception e) {
            log("Error en ObjetivoController", e);
            request.setAttribute("error", "Ha ocurrido un error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                crearObjetivo(request, response);
            } else if ("update".equals(action)) {
                actualizarObjetivo(request, response);
            } else if ("updateProgress".equals(action)) {
                actualizarProgreso(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (Exception e) {
            log("Error en ObjetivoController POST", e);
            request.setAttribute("error", "Ha ocurrido un error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    private void listarObjetivos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String usuarioId = obtenerUsuarioId(request);
        if (usuarioId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
        List<Objetivo> objetivosActivos = objetivoDAO.findObjetivosActivos(usuarioId);
        List<Objetivo> objetivosCompletados = objetivoDAO.findObjetivosCompletados(usuarioId);
        
        Long totalCompletados = objetivoDAO.countObjetivosCompletados(usuarioId);
        Double progresoPromedio = objetivoDAO.getProgresoPromedio(usuarioId);
        
        request.setAttribute("objetivos", objetivos);
        request.setAttribute("objetivosActivos", objetivosActivos);
        request.setAttribute("objetivosCompletados", objetivosCompletados);
        request.setAttribute("totalCompletados", totalCompletados);
        request.setAttribute("progresoPromedio", progresoPromedio);
        
        request.getRequestDispatcher("/WEB-INF/views/objetivos/lista.jsp").forward(request, response);
    }
    
    private void mostrarFormularioCrear(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/objetivos/crear.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Objetivo objetivo = objetivoDAO.findById(id).orElse(null);
            
            if (objetivo == null) {
                request.setAttribute("error", "Objetivo no encontrado");
                response.sendRedirect(request.getContextPath() + "/objetivos");
                return;
            }
            
            // Verificar que el objetivo pertenece al usuario actual
            String usuarioId = obtenerUsuarioId(request);
            if (!objetivo.getUsuarioId().equals(usuarioId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado");
                return;
            }
            
            request.setAttribute("objetivo", objetivo);
            request.getRequestDispatcher("/WEB-INF/views/objetivos/editar.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
        }
    }
    
    private void verObjetivo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Objetivo objetivo = objetivoDAO.findById(id).orElse(null);
            
            if (objetivo == null) {
                request.setAttribute("error", "Objetivo no encontrado");
                response.sendRedirect(request.getContextPath() + "/objetivos");
                return;
            }
            
            // Verificar que el objetivo pertenece al usuario actual
            String usuarioId = obtenerUsuarioId(request);
            if (!objetivo.getUsuarioId().equals(usuarioId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado");
                return;
            }
            
            // Obtener registros de progreso
            var registrosProgreso = objetivoDAO.findRegistrosProgreso(id);
            
            request.setAttribute("objetivo", objetivo);
            request.setAttribute("registrosProgreso", registrosProgreso);
            request.getRequestDispatcher("/WEB-INF/views/objetivos/detalle.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
        }
    }
    
    private void crearObjetivo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String usuarioId = obtenerUsuarioId(request);
        if (usuarioId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String fechaLimiteStr = request.getParameter("fechaLimite");
        
        // Validaciones básicas
        if (titulo == null || titulo.trim().isEmpty()) {
            request.setAttribute("error", "El título es obligatorio");
            request.getRequestDispatcher("/WEB-INF/views/objetivos/crear.jsp").forward(request, response);
            return;
        }
        
        LocalDateTime fechaLimite = null;
        if (fechaLimiteStr != null && !fechaLimiteStr.trim().isEmpty()) {
            try {
                fechaLimite = LocalDateTime.parse(fechaLimiteStr + "T23:59:59");
            } catch (Exception e) {
                request.setAttribute("error", "Formato de fecha inválido");
                request.getRequestDispatcher("/WEB-INF/views/objetivos/crear.jsp").forward(request, response);
                return;
            }
        }
        
        Objetivo objetivo = new Objetivo(titulo.trim(), descripcion, fechaLimite, usuarioId);
        objetivoDAO.save(objetivo);
        
        request.setAttribute("success", "Objetivo creado exitosamente");
        response.sendRedirect(request.getContextPath() + "/objetivos");
    }
    
    private void actualizarObjetivo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Objetivo objetivo = objetivoDAO.findById(id).orElse(null);
            
            if (objetivo == null) {
                response.sendRedirect(request.getContextPath() + "/objetivos");
                return;
            }
            
            // Verificar autorización
            String usuarioId = obtenerUsuarioId(request);
            if (!objetivo.getUsuarioId().equals(usuarioId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado");
                return;
            }
            
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String fechaLimiteStr = request.getParameter("fechaLimite");
            String estadoStr = request.getParameter("estado");
            
            if (titulo == null || titulo.trim().isEmpty()) {
                request.setAttribute("error", "El título es obligatorio");
                request.setAttribute("objetivo", objetivo);
                request.getRequestDispatcher("/WEB-INF/views/objetivos/editar.jsp").forward(request, response);
                return;
            }
            
            objetivo.setTitulo(titulo.trim());
            objetivo.setDescripcion(descripcion);
            
            if (fechaLimiteStr != null && !fechaLimiteStr.trim().isEmpty()) {
                try {
                    LocalDateTime fechaLimite = LocalDateTime.parse(fechaLimiteStr + "T23:59:59");
                    objetivo.setFechaLimite(fechaLimite);
                } catch (Exception e) {
                    // Mantener fecha actual si hay error
                }
            }
            
            if (estadoStr != null) {
                try {
                    Objetivo.EstadoObjetivo estado = Objetivo.EstadoObjetivo.valueOf(estadoStr);
                    objetivo.setEstado(estado);
                } catch (IllegalArgumentException e) {
                    // Mantener estado actual si hay error
                }
            }
            
            objetivoDAO.save(objetivo);
            
            response.sendRedirect(request.getContextPath() + "/objetivos?action=view&id=" + id);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
        }
    }
    
    private void actualizarProgreso(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String progresoStr = request.getParameter("progreso");
        String observaciones = request.getParameter("observaciones");
        
        if (idStr == null || progresoStr == null) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            int progreso = Integer.parseInt(progresoStr);
            
            // Validar rango
            if (progreso < 0 || progreso > 100) {
                request.setAttribute("error", "El progreso debe estar entre 0 y 100");
                response.sendRedirect(request.getContextPath() + "/objetivos?action=view&id=" + id);
                return;
            }
            
            objetivoDAO.actualizarProgreso(id, progreso, observaciones);
            
            response.sendRedirect(request.getContextPath() + "/objetivos?action=view&id=" + id);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
        }
    }
    
    private void eliminarObjetivo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/delete/")) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        String idStr = pathInfo.substring("/delete/".length());
        
        try {
            Long id = Long.parseLong(idStr);
            Objetivo objetivo = objetivoDAO.findById(id).orElse(null);
            
            if (objetivo != null) {
                // Verificar autorización
                String usuarioId = obtenerUsuarioId(request);
                if (objetivo.getUsuarioId().equals(usuarioId)) {
                    objetivoDAO.delete(id);
                    request.setAttribute("success", "Objetivo eliminado exitosamente");
                }
            }
            
        } catch (NumberFormatException e) {
            // ID inválido, ignorar
        }
        
        response.sendRedirect(request.getContextPath() + "/objetivos");
    }
    
    private void mostrarProgreso(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Objetivo objetivo = objetivoDAO.findById(id).orElse(null);
            
            if (objetivo == null) {
                response.sendRedirect(request.getContextPath() + "/objetivos");
                return;
            }
            
            // Verificar autorización
            String usuarioId = obtenerUsuarioId(request);
            if (!objetivo.getUsuarioId().equals(usuarioId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado");
                return;
            }
            
            request.setAttribute("objetivo", objetivo);
            request.getRequestDispatcher("/WEB-INF/views/objetivos/progreso.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/objetivos");
        }
    }
    
    private String obtenerUsuarioId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object user = session.getAttribute("user");
            if (user != null) {
                return user.toString(); // Asumiendo que el user es un String
            }
        }
        return null;
    }
}
