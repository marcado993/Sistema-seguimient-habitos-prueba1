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
import java.util.Optional;

/**
 * Controlador de Objetivos siguiendo el diagrama de secuencia.
 * Flujo: VentanaEstablecerObjetivo -> ControladorObjetivos -> ObjetivoDAO -> Objetivo
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
     * Crea los datos del objetivo (representa <<create>> create(datosObjetivo) en el diagrama)
     */
    public Objetivo crearDatosObjetivo(String nombre, String categoria, String frecuencia, String usuarioId) {
        Objetivo objetivo = new Objetivo();
        objetivo.setTitulo(nombre);
        objetivo.setDescripcion(categoria);
        objetivo.setUsuarioId(usuarioId);
        // Inicializar otros campos según frecuencia si es necesario
        objetivo.setProgreso(0);
        objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
        return objetivo;
    }
    
    /**
     * Persistir el objetivo usando DAO (representa la llamada al DAO en el diagrama)
     */
    public Objetivo persistirObjetivo(Objetivo objetivo) {
        return objetivoDAO.save(objetivo);
    }
    
    /**
     * Notificar éxito del registro (representa notificarExitoRegistro() en el diagrama)
     */
    public String notificarExitoRegistro(Objetivo nuevoObjetivo) {
        if (nuevoObjetivo != null && nuevoObjetivo.getId() != null) {
            return "Objetivo creado exitosamente con ID: " + nuevoObjetivo.getId();
        }
        return "Error al crear el objetivo";
    }
    
    /**
     * Limpiar el formulario (representa limpiarFormulario() en el diagrama)
     */
    public void limpiarFormulario(HttpServletRequest request) {
        request.removeAttribute("titulo");
        request.removeAttribute("descripcion");
        request.removeAttribute("frecuencia");
    }
    
    /**
     * Método de alto nivel que implementa la secuencia completa de creación
     * según el diagrama: recibir datos -> crearDatosObjetivo -> persistir -> notificar -> limpiar
     */
    protected void crearObjetivoSequence(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        if (usuarioId == null || usuarioId.isEmpty()) usuarioId = "usuario_demo";

        // 1) VentanaEstablecerObjetivo envía datos
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String frecuencia = request.getParameter("frecuencia");
        String fechaInicioStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        String estadoStr = request.getParameter("estado");

        System.out.println("[ControladorObjetivo] iniciar secuencia de creación de objetivo para usuario: " + usuarioId + " titulo=" + titulo);

        // 2) crearDatosObjetivo(nombre, categoria, frecuencia)
        Objetivo nuevoObjetivo = crearDatosObjetivo(titulo, descripcion, frecuencia, usuarioId);

        // Parsear fechas y estado si vienen
        try {
            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                nuevoObjetivo.setFechaCreacion(java.time.LocalDate.parse(fechaInicioStr).atStartOfDay());
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                nuevoObjetivo.setFechaLimite(java.time.LocalDate.parse(fechaFinStr).atStartOfDay());
            }
            if (estadoStr != null && !estadoStr.isEmpty()) {
                try {
                    nuevoObjetivo.setEstado(Objetivo.EstadoObjetivo.valueOf(estadoStr));
                } catch (IllegalArgumentException ex) {
                    nuevoObjetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
                }
            }
        } catch (Exception e) {
            System.err.println("[ControladorObjetivo] Error parseando fechas/estado: " + e.getMessage());
        }

        // 3) persistirObjetivo -> devuelve objetivoDAO (objetivo persistido)
        Objetivo objetivoPersistido = persistirObjetivo(nuevoObjetivo);
        System.out.println("[ControladorObjetivo] objetivoPersistido id=" + (objetivoPersistido != null ? objetivoPersistido.getId() : "null"));

        // 4) notificarExitoRegistro()
        String mensaje = notificarExitoRegistro(objetivoPersistido);
        session.setAttribute("mensaje", mensaje);

        // 5) limpiarFormulario()
        limpiarFormulario(request);

        // 6) preparar vista Planificar Objetivo (según diagrama muestra ventana Planificar Objetivo)
        List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
        request.setAttribute("objetivos", objetivos);
        request.setAttribute("objetivoRecienCreado", objetivoPersistido != null ? objetivoPersistido.getId() : null);

        request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
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
        } else if ("editar".equals(action)) {
            // Cargar objetivo para editar
            String objetivoIdStr = request.getParameter("id");
            if (objetivoIdStr != null) {
                try {
                    Long objetivoId = Long.parseLong(objetivoIdStr);
                    Optional<Objetivo> optObjetivo = objetivoDAO.findById(objetivoId);
                    if (optObjetivo.isPresent()) {
                        Objetivo objetivo = optObjetivo.get();
                        if (objetivo.getUsuarioId().equals(usuarioId)) {
                            request.setAttribute("objetivo", objetivo);
                            request.setAttribute("modoEdicion", true);
                            request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
                            return;
                        }
                    }
                    session.setAttribute("error", "Objetivo no encontrado o no tienes permiso");
                    response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=planificar");
                } catch (Exception e) {
                    System.err.println("❌ Error al cargar objetivo para editar: " + e.getMessage());
                    session.setAttribute("error", "Error al cargar objetivo");
                    response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=planificar");
                }
            }
        } else if ("eliminar".equals(action)) {
            // Eliminar objetivo
            String objetivoIdStr = request.getParameter("id");
            if (objetivoIdStr != null) {
                try {
                    Long objetivoId = Long.parseLong(objetivoIdStr);
                    Optional<Objetivo> optObjetivo = objetivoDAO.findById(objetivoId);
                    if (optObjetivo.isPresent()) {
                        Objetivo objetivo = optObjetivo.get();
                        if (objetivo.getUsuarioId().equals(usuarioId)) {
                            objetivoDAO.delete(objetivoId);
                            session.setAttribute("mensaje", "✅ Objetivo eliminado exitosamente");
                        } else {
                            session.setAttribute("error", "❌ No tienes permiso para eliminar este objetivo");
                        }
                    } else {
                        session.setAttribute("error", "❌ Objetivo no encontrado");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error al eliminar objetivo: " + e.getMessage());
                    session.setAttribute("error", "Error al eliminar objetivo");
                }
            }
            response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=planificar");
        } else {
            // Por defecto, mostrar formulario
            request.getRequestDispatcher("/WEB-INF/views/establecerObjetivo.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = "usuario_demo";
        }
        
        if ("crear".equals(action)) {
            // Ejecuta la secuencia de creación según diagrama
            crearObjetivoSequence(request, response);
            return;
        } else if ("actualizar".equals(action)) {
            // Actualizar objetivo existente
            String objetivoIdStr = request.getParameter("id");
            if (objetivoIdStr != null) {
                try {
                    Long objetivoId = Long.parseLong(objetivoIdStr);
                    Optional<Objetivo> optObjetivo = objetivoDAO.findById(objetivoId);
                    
                    if (optObjetivo.isPresent()) {
                        Objetivo objetivo = optObjetivo.get();
                        if (objetivo.getUsuarioId().equals(usuarioId)) {
                            // Actualizar campos
                            objetivo.setTitulo(request.getParameter("titulo"));
                            objetivo.setDescripcion(request.getParameter("descripcion"));
                            
                            String fechaInicioStr = request.getParameter("fechaInicio");
                            String fechaFinStr = request.getParameter("fechaFin");
                            String estadoStr = request.getParameter("estado");
                            
                            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                                objetivo.setFechaCreacion(java.time.LocalDate.parse(fechaInicioStr).atStartOfDay());
                            }
                            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                                objetivo.setFechaLimite(java.time.LocalDate.parse(fechaFinStr).atStartOfDay());
                            }
                            if (estadoStr != null && !estadoStr.isEmpty()) {
                                objetivo.setEstado(Objetivo.EstadoObjetivo.valueOf(estadoStr));
                            }
                            
                            objetivoDAO.save(objetivo); // save() hace merge si ya existe
                            session.setAttribute("mensaje", "✅ Objetivo actualizado exitosamente");
                            response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=planificar");
                            return;
                        }
                    }
                    session.setAttribute("error", "❌ No tienes permiso para editar este objetivo");
                } catch (Exception e) {
                    System.err.println("❌ Error al actualizar objetivo: " + e.getMessage());
                    session.setAttribute("error", "Error al actualizar objetivo");
                }
            }
            response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=planificar");
            return;
        }
        
        // si no es crear ni actualizar, procesar otras acciones si se requiere
        doGet(request, response);
    }
}
