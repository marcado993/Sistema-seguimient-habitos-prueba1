package com.example.servlet;

import com.example.dao.ObjetivoDAO;
import com.example.model.Objetivo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controlador de Objetivos según el diagrama de clases
 * Maneja las operaciones relacionadas con objetivos
 */
@WebServlet("/controlador-objetivos")
public class ControladorObjetivo extends HttpServlet {
    
    private ObjetivoDAO objetivoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        objetivoDAO = new ObjetivoDAO();
    }
    
    /**
     * Crear un nuevo objetivo
     * Basado en el diagrama de secuencia
     */
    public Objetivo crear(String nombre, String categoria, String frecuencia) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(nombre);
        objetivo.setDescripcion(categoria);
        // Configurar frecuencia según sea necesario
        
        return objetivoDAO.save(objetivo);
    }
    
    /**
     * Actualizar estado de un objetivo
     */
    public void actualizarEstado(Long objetivoId, String nuevoEstado) {
        Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
        
        if (objetivo != null) {
            try {
                Objetivo.EstadoObjetivo estado = Objetivo.EstadoObjetivo.valueOf(nuevoEstado.toUpperCase());
                objetivo.setEstado(estado);
                objetivoDAO.save(objetivo);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Crear datos del objetivo
     */
    public Objetivo crearDatosObjetivo(String nombre, String categoria, String frecuencia) {
        return crear(nombre, categoria, frecuencia);
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
     * Limpiar formulario
     */
    public void limpiarFormulario(HttpServletRequest request) {
        // Limpiar atributos del request
        request.removeAttribute("nombre");
        request.removeAttribute("categoria");
        request.removeAttribute("frecuencia");
    }
    
    /**
     * Devolver nuevo hábito (retorna el objetivo creado)
     */
    public Objetivo devolverNuevoHabito(Objetivo objetivo) {
        return objetivo;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = "usuario_demo"; // Usuario por defecto
        }
        
        System.out.println("🎯 ControladorObjetivo - GET action: " + action + ", usuarioId: " + usuarioId);
        
        if ("nuevo".equals(action)) {
            // Mostrar formulario para crear objetivo
            request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
        } else if ("planificar".equals(action)) {
            // Mostrar página de planificación con lista de objetivos
            try {
                List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
                System.out.println("📋 Objetivos encontrados para planificar: " + objetivos.size());
                request.setAttribute("objetivos", objetivos);
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
            } catch (Exception e) {
                System.err.println("❌ Error al cargar objetivos: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Error al cargar objetivos: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
            }
        } else if ("listar".equals(action)) {
            // Listar objetivos del usuario
            request.setAttribute("objetivos", objetivoDAO.findByUsuarioId(usuarioId));
            request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);
        } else {
            // Por defecto, mostrar formulario
            request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = "usuario_demo"; // Usuario por defecto
        }
        
        String action = request.getParameter("action");
        
        if ("crear".equals(action)) {
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String fechaInicioStr = request.getParameter("fechaInicio");
            String fechaFinStr = request.getParameter("fechaFin");
            String estadoStr = request.getParameter("estado");
            
            System.out.println("📝 Creando objetivo: " + titulo);
            
            try {
                Objetivo nuevoObjetivo = new Objetivo();
                nuevoObjetivo.setTitulo(titulo);
                nuevoObjetivo.setDescripcion(descripcion);
                nuevoObjetivo.setUsuarioId(usuarioId);
                
                // Parsear estado
                if (estadoStr != null && !estadoStr.isEmpty()) {
                    try {
                        nuevoObjetivo.setEstado(Objetivo.EstadoObjetivo.valueOf(estadoStr));
                    } catch (IllegalArgumentException e) {
                        nuevoObjetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
                    }
                } else {
                    nuevoObjetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
                }
                
                nuevoObjetivo.setProgreso(0);
                
                // Parsear fechas si existen
                if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                    nuevoObjetivo.setFechaCreacion(java.time.LocalDate.parse(fechaInicioStr).atStartOfDay());
                }
                
                if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                    nuevoObjetivo.setFechaLimite(java.time.LocalDate.parse(fechaFinStr).atStartOfDay());
                }
                
                // Guardar objetivo
                nuevoObjetivo = objetivoDAO.save(nuevoObjetivo);
                System.out.println("✅ Objetivo guardado con ID: " + nuevoObjetivo.getId());
                
                if (nuevoObjetivo != null && nuevoObjetivo.getId() != null) {
                    String mensaje = notificarExitoRegistro(nuevoObjetivo);
                    session.setAttribute("mensaje", mensaje);
                    session.setAttribute("ultimoObjetivoCreado", nuevoObjetivo.getId());
                    limpiarFormulario(request);
                    
                    // Cargar TODOS los objetivos del usuario
                    List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
                    System.out.println("📋 Cargando objetivos para planificar. Total: " + objetivos.size());
                    
                    request.setAttribute("objetivos", objetivos);
                    request.setAttribute("objetivoRecienCreado", nuevoObjetivo.getId());
                    
                    // Forward a planificar objetivo
                    request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
                } else {
                    System.err.println("❌ Error: Objetivo guardado pero ID es null");
                    response.sendRedirect("controlador-objetivos?action=nuevo&error=save");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al crear objetivo: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Error al crear el objetivo: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
            }
        }
    }
}
