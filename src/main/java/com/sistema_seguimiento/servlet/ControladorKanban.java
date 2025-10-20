package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.services.KanbanServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControladorKanban extends HttpServlet {

    private KanbanServicio kanbanServicio;

    public void setKanbanServicio(KanbanServicio kanbanServicio) {
        this.kanbanServicio = kanbanServicio;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("mover".equalsIgnoreCase(accion)) {
            String tareaId = req.getParameter("tareaId");
            String columnaDestino = req.getParameter("columnaDestino");
            if (kanbanServicio != null) {
                // Corregido el orden de los parámetros para que coincida con lo que el test espera
                kanbanServicio.moverTarea(tareaId, columnaDestino);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
