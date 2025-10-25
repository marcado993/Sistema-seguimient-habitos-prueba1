package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.model.Usuario;
import com.sistema_seguimiento.services.KanbanServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ControladorKanban", urlPatterns = {"/controlador-kanban"})
public class ControladorKanban extends HttpServlet {

    private KanbanServicio kanbanServicio;

    @Override
    public void init() throws ServletException {
        super.init();
        // kanbanServicio será inyectado mediante setter si es necesario
    }

    public void setKanbanServicio(KanbanServicio kanbanServicio) {
        this.kanbanServicio = kanbanServicio;
    }

    /**
     * Obtener usuarioId desde la sesión
     */
    private Integer getUsuarioIdFromSession(HttpSession session) {
        if (session == null) {
            return null;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            return usuario.getId();
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer usuarioId = getUsuarioIdFromSession(session);
        
        if (usuarioId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            // Listar tareas del kanban del usuario
            response.sendRedirect(request.getContextPath() + "/WEB-INF/views/kanban.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer usuarioId = getUsuarioIdFromSession(session);
        
        if (usuarioId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String accion = req.getParameter("accion");
        if ("mover".equalsIgnoreCase(accion)) {
            String tareaId = req.getParameter("tareaId");
            String columnaDestino = req.getParameter("columnaDestino");
            if (kanbanServicio != null && tareaId != null) {
                try {
                    // Procesar movimiento de tarea
                    kanbanServicio.moverTarea(tareaId, columnaDestino);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception e) {
                    System.err.println("Error al mover tarea: " + e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
