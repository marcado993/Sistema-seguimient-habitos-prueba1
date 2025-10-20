package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.dao.ObjetivoDAO;
import com.sistema_seguimiento.model.Objetivo;
import com.sistema_seguimiento.model.RegistroProgreso;
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
 * Controlador de Objetivos siguiendo el diagrama de secuencia.
 * Flujo: VentanaEstablecerObjetivo -> ControladorObjetivos -> ObjetivoDAO -> Objetivo
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

    // =======================
    // Lógica unificada de /planificar (antes en ControladorPlanificarObjetivo)
    // =======================
    private void listarPlanificaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuarioId = request.getParameter("usuarioId");
        if (usuarioId == null || usuarioId.isEmpty()) {
            HttpSession session = request.getSession();
            usuarioId = (String) session.getAttribute("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                usuarioId = "usuario_demo";
            }
        }
        List<Objetivo> objetivos = objetivoDAO.findByUsuarioId(usuarioId);
        Long completados = objetivoDAO.countObjetivosCompletados(usuarioId);
        Double progresoPromedio = objetivoDAO.getProgresoPromedio(usuarioId);
        request.setAttribute("objetivos", objetivos);
        request.setAttribute("objetivosCompletados", completados);
        request.setAttribute("progresoPromedio", progresoPromedio);
        request.setAttribute("usuarioId", usuarioId);
        request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
    }

    private void mostrarFormularioPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicionPlan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
            if (objetivo != null) {
                request.setAttribute("objetivo", objetivo);
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "❌ Objetivo no encontrado");
                response.sendRedirect(request.getContextPath() + "/planificar");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error al cargar el objetivo: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/planificar");
        }
    }

    private void verDetallePlan(HttpServletRequest request, HttpServletResponse response)
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
                response.sendRedirect(request.getContextPath() + "/planificar");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error al cargar el detalle: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/planificar");
        }
    }

    private void planificarObjetivo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String usuarioId = request.getParameter("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                HttpSession session = request.getSession();
                usuarioId = (String) session.getAttribute("usuarioId");
                if (usuarioId == null || usuarioId.isEmpty()) usuarioId = "usuario_demo";
            }
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String fechaLimiteStr = request.getParameter("fechaLimite");
            if (titulo == null || titulo.trim().isEmpty()) {
                request.setAttribute("error", "El título es obligatorio");
                request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
                return;
            }
            Objetivo objetivo = new Objetivo();
            objetivo.setTitulo(titulo);
            objetivo.setDescripcion(descripcion);
            objetivo.setUsuarioId(usuarioId);
            objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
            objetivo.setProgreso(0);
            if (fechaLimiteStr != null && !fechaLimiteStr.isEmpty()) {
                LocalDateTime fechaLimite = LocalDate.parse(fechaLimiteStr).atStartOfDay();
                objetivo.setFechaLimite(fechaLimite);
            }
            Objetivo objetivoGuardado = objetivoDAO.save(objetivo);
            if (objetivoGuardado != null) {
                request.getSession().setAttribute("mensaje", "✅ Objetivo creado exitosamente");
                response.sendRedirect(request.getContextPath() + "/planificar?action=listar&usuarioId=" + usuarioId);
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

    private void actualizarPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
            if (objetivo != null) {
                String usuarioId = objetivo.getUsuarioId();
                String titulo = request.getParameter("titulo");
                String descripcion = request.getParameter("descripcion");
                String fechaLimiteStr = request.getParameter("fechaLimite");
                String estadoStr = request.getParameter("estado");
                String progresoStr = request.getParameter("progreso");
                if (titulo != null && !titulo.trim().isEmpty()) objetivo.setTitulo(titulo);
                if (descripcion != null) objetivo.setDescripcion(descripcion);
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
                response.sendRedirect(request.getContextPath() + "/planificar?action=listar&usuarioId=" + usuarioId);
            } else {
                request.getSession().setAttribute("error", "❌ Objetivo no encontrado");
                response.sendRedirect(request.getContextPath() + "/planificar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar el objetivo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
        }
    }

    private void eliminarPlanificacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            String usuarioId = request.getParameter("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                HttpSession session = request.getSession();
                usuarioId = (String) session.getAttribute("usuarioId");
                if (usuarioId == null || usuarioId.isEmpty()) usuarioId = "usuario_demo";
            }
            objetivoDAO.delete(objetivoId);
            request.getSession().setAttribute("mensaje", "✅ Objetivo eliminado exitosamente");
            response.sendRedirect(request.getContextPath() + "/planificar?action=listar&usuarioId=" + usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al eliminar el objetivo: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/planificar");
        }
    }

    private void actualizarProgreso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long objetivoId = Long.parseLong(request.getParameter("id"));
            int nuevoProgreso = Integer.parseInt(request.getParameter("progreso"));
            String observaciones = request.getParameter("observaciones");
            String usuarioId = request.getParameter("usuarioId");
            if (usuarioId == null || usuarioId.isEmpty()) {
                HttpSession session = request.getSession();
                usuarioId = (String) session.getAttribute("usuarioId");
                if (usuarioId == null || usuarioId.isEmpty()) usuarioId = "usuario_demo";
            }
            objetivoDAO.actualizarProgreso(objetivoId, nuevoProgreso, observaciones);
            request.getSession().setAttribute("mensaje", "✅ Progreso actualizado exitosamente");
            response.sendRedirect(request.getContextPath() + "/planificar?action=listar&usuarioId=" + usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar el progreso: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/planificar");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("/planificar".equals(request.getServletPath())) {
            if (action == null) action = "listar";
            switch (action) {
                case "nuevo":
                    mostrarFormularioPlanificacion(request, response);
                    return;
                case "editar":
                    mostrarFormularioEdicionPlan(request, response);
                    return;
                case "detalle":
                    verDetallePlan(request, response);
                    return;
                case "listar":
                default:
                    listarPlanificaciones(request, response);
                    return;
            }
        }
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
        if ("/planificar".equals(request.getServletPath())) {
            if (action == null) action = "planificar";
            switch (action) {
                case "planificar":
                case "crear":
                    planificarObjetivo(request, response);
                    return;
                case "actualizar":
                    actualizarPlanificacion(request, response);
                    return;
                case "actualizarProgreso":
                    actualizarProgreso(request, response);
                    return;
                case "eliminar":
                    eliminarPlanificacion(request, response);
                    return;
                default:
                    response.sendRedirect(request.getContextPath() + "/planificar");
                    return;
            }
        }
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
