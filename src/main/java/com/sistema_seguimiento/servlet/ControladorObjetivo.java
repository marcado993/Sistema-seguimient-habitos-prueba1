package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.dao.ObjetivoDAO;
import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de Objetivos
 */
@WebServlet(urlPatterns = {"/controlador-objetivos", "/planificar"})
public class ControladorObjetivo extends HttpServlet {
    
    private ObjetivoDAO objetivoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        objetivoDAO = new ObjetivoDAO();
    }
    
    /**
     * Obtener el usuarioId de la sesión
     */
    private Integer getUsuarioIdFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return (usuario != null) ? usuario.getId() : null;
    }
    
    /**
     * Crea los datos del objetivo
     */
    public Objetivo crearDatosObjetivo(String nombre, String descripcion, String frecuencia, Integer usuarioId) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(nombre);
        objetivo.setDescripcion(descripcion);
        objetivo.setUsuarioId(usuarioId);
        objetivo.setProgresoActual(0);
        objetivo.setMeta(100);
        objetivo.setEstado(Objetivo.EstadoObjetivo.PENDIENTE);
        objetivo.setFechaInicio(LocalDate.now());
        return objetivo;
    }
    
    /**
     * Persistir el objetivo usando DAO
     */
    public Objetivo persistirObjetivo(Objetivo objetivo) {
        return objetivoDAO.save(objetivo);
    }
    
    /**
     * Notificar éxito del registro
     */
    public String notificarExitoRegistro(Objetivo nuevoObjetivo) {
        if (nuevoObjetivo != null && nuevoObjetivo.getId() != null) {
            return "Objetivo creado exitosamente con ID: " + nuevoObjetivo.getId();
        }
        return "Error al crear el objetivo";
    }
    
    /**
     * Limpiar el formulario
     */
    public void limpiarFormulario(HttpServletRequest request) {
        request.removeAttribute("titulo");
        request.removeAttribute("descripcion");
        request.removeAttribute("frecuencia");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String action = request.getParameter("action");
            String requestURI = request.getRequestURI();
            HttpSession session = request.getSession(false);
            Integer usuarioId = getUsuarioIdFromSession(session);
            
            if (usuarioId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            // Si accede a /planificar sin action, mostrar vista de planificación
            if (requestURI.endsWith("/planificar") && action == null) {
                mostrarVistaPlanificacion(request, response, usuarioId);
            } else if ("listar".equals(action)) {
                listarObjetivos(request, response, usuarioId);
            } else if ("editar".equals(action)) {
                mostrarFormularioEdicion(request, response);
            } else {
                mostrarFormularioCreacion(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en ControladorObjetivo.doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String action = request.getParameter("action");
            HttpSession session = request.getSession(false);
            Integer usuarioId = getUsuarioIdFromSession(session);
            
            if (usuarioId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            if ("crear".equals(action)) {
                crearObjetivo(request, response, usuarioId);
            } else if ("actualizar".equals(action)) {
                actualizarObjetivo(request, response, usuarioId);
            } else if ("eliminar".equals(action)) {
                eliminarObjetivo(request, response, usuarioId);
            }
        } catch (Exception e) {
            System.err.println("Error en ControladorObjetivo.doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    private void crearObjetivo(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) 
            throws ServletException, IOException {
        
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String frecuencia = request.getParameter("frecuencia");
        String fechaInicioStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        
        System.out.println("[ControladorObjetivo] Creando objetivo para usuario: " + usuarioId);
        
        Objetivo nuevoObjetivo = crearDatosObjetivo(titulo, descripcion, frecuencia, usuarioId);
        
        try {
            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                nuevoObjetivo.setFechaInicio(LocalDate.parse(fechaInicioStr));
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                nuevoObjetivo.setFechaFin(LocalDate.parse(fechaFinStr));
            }
        } catch (Exception e) {
            System.out.println("Error al parsear fechas: " + e.getMessage());
        }
        
        Objetivo objetivoPersistido = persistirObjetivo(nuevoObjetivo);
        String mensaje = notificarExitoRegistro(objetivoPersistido);
        
        HttpSession session = request.getSession();
        session.setAttribute("mensaje", mensaje);
        session.setAttribute("objetivoRecienCreado", objetivoPersistido.getId());
        limpiarFormulario(request);
        
        // Redirigir a planificar para que el usuario pueda crear hábitos asociados
        response.sendRedirect(request.getContextPath() + "/planificar");
    }
    
    private void listarObjetivos(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) 
            throws ServletException, IOException {
        
        List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
        request.setAttribute("objetivos", objetivos);
        request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);
    }
    
    private void mostrarVistaPlanificacion(HttpServletRequest request, HttpServletResponse response, Integer usuarioId)
            throws ServletException, IOException {
        
        List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
        request.setAttribute("objetivos", objetivos);
        request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
    }
    
    private void mostrarFormularioCreacion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                Integer id = Integer.parseInt(idStr);
                Optional<Objetivo> objetivo = objetivoDAO.findById(id);
                if (objetivo.isPresent()) {
                    request.setAttribute("objetivo", objetivo.get());
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + idStr);
            }
        }
        
        request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
    }
    
    private void actualizarObjetivo(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                Integer id = Integer.parseInt(idStr);
                Optional<Objetivo> objetivoOpt = objetivoDAO.findById(id);
                
                if (objetivoOpt.isPresent()) {
                    Objetivo objetivo = objetivoOpt.get();
                    
                    // Actualizar campos
                    String titulo = request.getParameter("titulo");
                    String descripcion = request.getParameter("descripcion");
                    String estadoStr = request.getParameter("estado");
                    String progresoStr = request.getParameter("progreso");
                    String fechaFinStr = request.getParameter("fechaFin");
                    
                    if (titulo != null) objetivo.setTitulo(titulo);
                    if (descripcion != null) objetivo.setDescripcion(descripcion);
                    if (estadoStr != null) {
                        try {
                            objetivo.setEstado(Objetivo.EstadoObjetivo.valueOf(estadoStr));
                        } catch (IllegalArgumentException e) {
                            // Mantener estado actual
                        }
                    }
                    if (progresoStr != null) {
                        try {
                            objetivo.setProgresoActual(Integer.parseInt(progresoStr));
                        } catch (NumberFormatException e) {
                            // Mantener progreso actual
                        }
                    }
                    if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                        objetivo.setFechaFin(LocalDate.parse(fechaFinStr));
                    }
                    
                    objetivo.setFechaActualizacion(LocalDateTime.now());
                    objetivoDAO.save(objetivo);
                    
                    System.out.println("Objetivo actualizado: " + id);
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + idStr);
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=listar");
    }
    
    private void eliminarObjetivo(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                Integer id = Integer.parseInt(idStr);
                objetivoDAO.delete(id);
                System.out.println("Objetivo eliminado: " + id);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + idStr);
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=listar");
    }
}
