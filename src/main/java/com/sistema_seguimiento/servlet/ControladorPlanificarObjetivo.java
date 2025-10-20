package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.dao.ObjetivoDAO;
import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.RegistroProgreso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para planificar objetivos
 * Gestiona la estrategia, frecuencia y tiempo necesario para cumplir objetivos
 */
@WebServlet("/planificar")
public class ControladorPlanificarObjetivo extends HttpServlet {
    
    private ObjetivoDAO objetivoDAO;
    
    @Override
    public void init() throws ServletException {
        objetivoDAO = new ObjetivoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "listar";
        }
        
        switch (action) {
            case "nuevo":
                mostrarFormularioPlanificacion(request, response);
                break;
            case "editar":
                mostrarFormularioEdicion(request, response);
                break;
            case "detalle":
                verDetalle(request, response);
                break;
            case "listar":
            default:
                listarPlanificaciones(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "planificar";
        }
        
        switch (action) {
            case "planificar":
            case "crear":
                planificarObjetivo(request, response);
                break;
            case "actualizar":
                actualizarPlanificacion(request, response);
                break;
            case "actualizarProgreso":
                actualizarProgreso(request, response);
                break;
            case "eliminar":
                eliminarPlanificacion(request, response);
                break;
            default:
                response.sendRedirect("planificar");
                break;
        }
    }
    
    /**
     * Muestra el formulario para planificar un nuevo objetivo
     */
    private void mostrarFormularioPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para editar una planificación existente
     */
    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
            
            if (objetivo != null) {
                request.setAttribute("objetivo", objetivo);
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "❌ Objetivo no encontrado");
                response.sendRedirect("planificar");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error al cargar el objetivo: " + e.getMessage());
            response.sendRedirect("planificar");
        }
    }
    
    /**
     * Planifica un nuevo objetivo
     */
    private void planificarObjetivo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener parámetros del formulario
            String usuarioId = request.getParameter("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                usuarioId = "usuario_demo"; // Usuario por defecto
            }
            
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String fechaLimiteStr = request.getParameter("fechaLimite");
            
            // Validaciones básicas
            if (titulo == null || titulo.trim().isEmpty()) {
                request.setAttribute("error", "El título es obligatorio");
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
                return;
            }
            
            // Crear nuevo objetivo
            Objetivo objetivo = new Objetivo();
            objetivo.setTitulo(titulo);
            objetivo.setDescripcion(descripcion);
            objetivo.setUsuarioId(usuarioId);
            objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
            objetivo.setProgreso(0);
            
            // Parsear fecha límite si existe
            if (fechaLimiteStr != null && !fechaLimiteStr.isEmpty()) {
                LocalDateTime fechaLimite = LocalDate.parse(fechaLimiteStr).atStartOfDay();
                objetivo.setFechaLimite(fechaLimite);
            }
            
            // Guardar objetivo
            Objetivo objetivoGuardado = objetivoDAO.save(objetivo);
            
            if (objetivoGuardado != null) {
                request.getSession().setAttribute("mensaje", "✅ Objetivo creado exitosamente");
                response.sendRedirect("planificar?action=listar&usuarioId=" + usuarioId);
            } else {
                request.setAttribute("error", "Error al guardar el objetivo");
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar el objetivo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualiza una planificación existente
     */
    private void actualizarPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
            
            if (objetivo != null) {
                String usuarioId = objetivo.getUsuarioId();
                
                // Actualizar campos
                String titulo = request.getParameter("titulo");
                String descripcion = request.getParameter("descripcion");
                String fechaLimiteStr = request.getParameter("fechaLimite");
                String estadoStr = request.getParameter("estado");
                String progresoStr = request.getParameter("progreso");
                
                if (titulo != null && !titulo.trim().isEmpty()) {
                    objetivo.setTitulo(titulo);
                }
                
                if (descripcion != null) {
                    objetivo.setDescripcion(descripcion);
                }
                
                if (fechaLimiteStr != null && !fechaLimiteStr.isEmpty()) {
                    LocalDateTime fechaLimite = LocalDate.parse(fechaLimiteStr).atStartOfDay();
                    objetivo.setFechaLimite(fechaLimite);
                }
                
                if (estadoStr != null && !estadoStr.isEmpty()) {
                    objetivo.setEstado(Objetivo.EstadoObjetivo.valueOf(estadoStr));
                }
                
                if (progresoStr != null && !progresoStr.isEmpty()) {
                    int progreso = Integer.parseInt(progresoStr);
                    objetivo.setProgreso(progreso);
                }
                
                objetivoDAO.save(objetivo);
                
                request.getSession().setAttribute("mensaje", "✅ Objetivo actualizado exitosamente");
                response.sendRedirect("planificar?action=listar&usuarioId=" + usuarioId);
            } else {
                request.getSession().setAttribute("error", "❌ Objetivo no encontrado");
                response.sendRedirect("planificar");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar el objetivo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
        }
    }
    
    /**
     * Elimina una planificación
     */
    private void eliminarPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            String usuarioId = request.getParameter("usuarioId");
            
            if (usuarioId == null || usuarioId.isEmpty()) {
                usuarioId = "usuario_demo";
            }
            
            objetivoDAO.delete(objetivoId);
            
            request.getSession().setAttribute("mensaje", "✅ Objetivo eliminado exitosamente");
            response.sendRedirect("planificar?action=listar&usuarioId=" + usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al eliminar el objetivo: " + e.getMessage());
            response.sendRedirect("planificar");
        }
    }
    
    /**
     * Lista todas las planificaciones de objetivos
     */
    private void listarPlanificaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String usuarioId = request.getParameter("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                usuarioId = "usuario_demo"; // Usuario por defecto
            }
            
            // Obtener todos los objetivos del usuario
            List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
            
            // Obtener estadísticas
            Long completados = objetivoDAO.countObjetivosCompletados(usuarioId);
            Double progresoPromedio = objetivoDAO.getProgresoPromedio(usuarioId);
            
            request.setAttribute("objetivos", objetivos);
            request.setAttribute("objetivosCompletados", completados);
            request.setAttribute("progresoPromedio", progresoPromedio);
            request.setAttribute("usuarioId", usuarioId);
            
            request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar los objetivos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualiza el progreso de un objetivo
     */
    private void actualizarProgreso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            int nuevoProgreso = Integer.parseInt(request.getParameter("progreso"));
            String observaciones = request.getParameter("observaciones");
            String usuarioId = request.getParameter("usuarioId");
            
            if (usuarioId == null || usuarioId.isEmpty()) {
                usuarioId = "usuario_demo";
            }
            
            // Actualizar progreso usando el DAO
            objetivoDAO.actualizarProgreso(objetivoId, nuevoProgreso, observaciones);
            
            request.getSession().setAttribute("mensaje", "✅ Progreso actualizado exitosamente");
            response.sendRedirect("planificar?action=listar&usuarioId=" + usuarioId);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar el progreso: " + e.getMessage());
            response.sendRedirect("planificar");
        }
    }
    
    /**
     * Ver detalle de un objetivo
     */
    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
            
            if (objetivo != null) {
                List<RegistroProgreso> registros = objetivoDAO.findRegistrosProgreso(objetivoId);
                
                request.setAttribute("objetivo", objetivo);
                request.setAttribute("registros", registros);
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "❌ Objetivo no encontrado");
                response.sendRedirect("planificar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar el detalle: " + e.getMessage());
            response.sendRedirect("planificar");
        }
    }
}
