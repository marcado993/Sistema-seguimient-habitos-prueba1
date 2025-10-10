package com.example.servlet;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.RegistroHabito;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador de Hábitos según el diagrama de clases
 * Maneja las operaciones relacionadas con hábitos
 */
@WebServlet("/controlador-habitos")
public class ControladorHabitos extends HttpServlet {
    
    private HabitoDAO habitoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoDAO = new HabitoDAO();
    }
    
    /**
     * Registrar cumplimiento de un hábito
     * Basado en el diagrama de secuencia
     */
    public Habito registrarCumplimiento(Long habitoId, LocalDate fecha, String observacion) {
        try {
            Habito habito = habitoDAO.findById(habitoId).orElse(null);
            
            if (habito == null) {
                return null;
            }
            
            // Registrar el cumplimiento del hábito
            habitoDAO.registrarCompletado(habitoId, fecha, observacion);
            
            return habito;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtener registros de un hábito en un rango de fechas
     */
    public List<RegistroHabito> obtenerRegistros(Long habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            return habitoDAO.findRegistrosByRango(habitoId, fechaInicio, fechaFin);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtener todos los registros de hoy del usuario
     */
    public List<RegistroHabito> obtenerRegistrosDeHoy(String usuarioId) {
        try {
            return habitoDAO.findRegistrosDeHoy(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Buscar hábito por ID
     */
    public Habito buscarHabito(Long habitoId) {
        return habitoDAO.findById(habitoId).orElse(null);
    }
    
    /**
     * Crear ficha de racha actual
     */
    public int crearFichaRacha(Habito habito, String estado, boolean cumplido) {
        if (habito != null) {
            return habito.calcularRachaActual();
        }
        return 0;
    }
    
    /**
     * Crear nuevo registro de hábito
     */
    public RegistroHabito crearNuevoRegistro(Habito habito, LocalDate fecha, String observacion) {
        try {
            RegistroHabito registro = new RegistroHabito(habito, fecha, 1, observacion);
            return habitoDAO.saveRegistro(registro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtener lista de hábitos del usuario
     */
    public List<Habito> listarHabitosUsuario(String usuarioId) {
        try {
            return habitoDAO.findByUsuarioId(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Guardar o actualizar hábito
     */
    public Habito guardarHabito(Habito habito) {
        try {
            return habitoDAO.save(habito);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Eliminar hábito (soft delete)
     */
    public boolean eliminarHabito(Long habitoId) {
        try {
            habitoDAO.delete(habitoId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtener estadísticas del usuario
     */
    public Long obtenerHabitosCompletadosHoy(String usuarioId) {
        try {
            return habitoDAO.countHabitosCompletadosHoy(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    /**
     * Obtener porcentaje de completado de la semana
     */
    public Double obtenerPorcentajeSemana(String usuarioId) {
        try {
            return habitoDAO.getPorcentajeCompletadoSemana(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String usuarioId = request.getParameter("usuarioId");
        
        if (usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = "usuario_demo"; // Usuario por defecto para pruebas
        }
        
        try {
            if ("list".equals(action)) {
                // Listar hábitos del usuario
                List<Habito> habitos = listarHabitosUsuario(usuarioId);
                request.setAttribute("habitos", habitos);
                request.getRequestDispatcher("/WEB-INF/views/registroHabito.jsp").forward(request, response);
                
            } else if ("registrar".equals(action)) {
                // Registrar cumplimiento
                String habitoIdStr = request.getParameter("habitoId");
                String observacion = request.getParameter("observacion");
                
                if (habitoIdStr != null) {
                    Long habitoId = Long.parseLong(habitoIdStr);
                    Habito habito = registrarCumplimiento(habitoId, LocalDate.now(), observacion);
                    
                    if (habito != null) {
                        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&success=true");
                    } else {
                        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=true");
                    }
                }
                
            } else if ("view".equals(action)) {
                // Ver seguimiento de hábitos del usuario
                String habitoIdStr = request.getParameter("habitoId");
                
                if (habitoIdStr != null && !habitoIdStr.isEmpty()) {
                    // Ver detalle de un hábito específico
                    Long habitoId = Long.parseLong(habitoIdStr);
                    Habito habito = buscarHabito(habitoId);
                    
                    if (habito != null) {
                        LocalDate hoy = LocalDate.now();
                        LocalDate hace30Dias = hoy.minusDays(30);
                        List<RegistroHabito> registros = obtenerRegistros(habitoId, hace30Dias, hoy);
                        
                        request.setAttribute("habito", habito);
                        request.setAttribute("registros", registros);
                        request.setAttribute("racha", habito.calcularRachaActual());
                        request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("controlador-habitos?action=list&error=notfound");
                    }
                } else {
                    // Ver todos los hábitos del usuario (vista de seguimiento general)
                    System.out.println("📊 Cargando vista de seguimiento para usuario: " + usuarioId);
                    List<Habito> habitos = listarHabitosUsuario(usuarioId);
                    System.out.println("📋 Hábitos encontrados: " + habitos.size());
                    request.setAttribute("habitos", habitos);
                    request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);
                }
                
            } else if ("delete".equals(action)) {
                // Eliminar hábito
                String habitoIdStr = request.getParameter("habitoId");
                if (habitoIdStr != null) {
                    Long habitoId = Long.parseLong(habitoIdStr);
                    boolean success = eliminarHabito(habitoId);
                    
                    if (success) {
                        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&deleted=true");
                    } else {
                        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=delete");
                    }
                }
                
            } else {
                // Por defecto, listar hábitos
                response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("controlador-habitos?action=list&error=exception");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String usuarioId = request.getParameter("usuarioId");
        
        if (usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = "usuario_demo"; // Usuario por defecto para pruebas
        }
        
        try {
            if ("crear-con-objetivo".equals(action)) {
                // Crear hábito asociado a un objetivo
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String frecuenciaStr = request.getParameter("frecuencia");
                String metaDiariaStr = request.getParameter("metaDiaria");
                String objetivoIdStr = request.getParameter("objetivoId");
                String fechaInicioStr = request.getParameter("fechaInicio");
                
                Habito habito = new Habito();
                habito.setNombre(nombre);
                habito.setDescripcion(descripcion);
                habito.setUsuarioId(usuarioId);
                
                if (frecuenciaStr != null && !frecuenciaStr.isEmpty()) {
                    habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr));
                }
                
                if (metaDiariaStr != null && !metaDiariaStr.isEmpty()) {
                    habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
                }
                
                if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                    habito.setFechaInicio(LocalDate.parse(fechaInicioStr));
                }
                
                // Asociar con objetivo si se proporciona
                if (objetivoIdStr != null && !objetivoIdStr.isEmpty()) {
                    Long objetivoId = Long.parseLong(objetivoIdStr);
                    com.example.dao.ObjetivoDAO objetivoDAO = new com.example.dao.ObjetivoDAO();
                    com.example.model.Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
                    if (objetivo != null) {
                        habito.setObjetivo(objetivo);
                    }
                }
                
                Habito habitoGuardado = guardarHabito(habito);
                
                if (habitoGuardado != null) {
                    request.getSession().setAttribute("mensaje", "✅ Hábito '" + nombre + "' creado y asociado al objetivo exitosamente");
                    response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&success=true");
                } else {
                    response.sendRedirect("controlador-objetivos?action=nuevo&error=save");
                }
                
            } else if ("create".equals(action) || "update".equals(action)) {
                // Crear o actualizar hábito
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String frecuenciaStr = request.getParameter("frecuencia");
                String metaDiariaStr = request.getParameter("metaDiaria");
                String habitoIdStr = request.getParameter("habitoId");
                
                Habito habito;
                if (habitoIdStr != null && !habitoIdStr.isEmpty()) {
                    // Actualizar hábito existente
                    Long habitoId = Long.parseLong(habitoIdStr);
                    habito = buscarHabito(habitoId);
                    if (habito == null) {
                        response.sendRedirect("controlador-habitos?action=list&error=notfound");
                        return;
                    }
                } else {
                    // Crear nuevo hábito
                    habito = new Habito();
                    habito.setUsuarioId(usuarioId);
                }
                
                // Actualizar datos
                habito.setNombre(nombre);
                habito.setDescripcion(descripcion);
                
                if (frecuenciaStr != null && !frecuenciaStr.isEmpty()) {
                    habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr));
                }
                
                if (metaDiariaStr != null && !metaDiariaStr.isEmpty()) {
                    habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
                }
                
                Habito habitoGuardado = guardarHabito(habito);
                
                if (habitoGuardado != null) {
                    response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&saved=true");
                } else {
                    response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=save");
                }
                
            } else if ("registrar".equals(action)) {
                // Registrar cumplimiento vía POST
                String habitoIdStr = request.getParameter("habitoId");
                String observacion = request.getParameter("observacion");
                String fechaStr = request.getParameter("fecha");
                String estado = request.getParameter("estado"); // CUMPLIDO, NO_CUMPLIDO, PARCIAL
                String estadoAnimo = request.getParameter("estadoAnimo");
                
                LocalDate fecha = (fechaStr != null && !fechaStr.isEmpty()) 
                    ? LocalDate.parse(fechaStr) 
                    : LocalDate.now();
                
                if (habitoIdStr != null) {
                    Long habitoId = Long.parseLong(habitoIdStr);
                    Habito habito = buscarHabito(habitoId);
                    
                    if (habito != null) {
                        // Crear el registro con el estado correspondiente
                        RegistroHabito registro = new RegistroHabito();
                        registro.setHabito(habito);
                        registro.setFecha(fecha);
                        registro.setObservacion(observacion);
                        registro.setEstadoAnimo(estadoAnimo);
                        
                        // Determinar el valor de "completado" basado en el estado
                        Integer metaDiaria = habito.getMetaDiaria() != null ? habito.getMetaDiaria() : 1;
                        
                        if ("CUMPLIDO".equals(estado)) {
                            registro.setCompletado(metaDiaria); // Cumplió la meta
                        } else if ("PARCIAL".equals(estado)) {
                            registro.setCompletado(metaDiaria / 2); // Cumplió parcialmente
                        } else { // NO_CUMPLIDO
                            registro.setCompletado(0); // No cumplió
                        }
                        
                        // Guardar el registro
                        RegistroHabito registroGuardado = habitoDAO.saveRegistro(registro);
                        
                        if (registroGuardado != null) {
                            response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&success=true");
                        } else {
                            response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=register");
                        }
                    } else {
                        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=notfound");
                    }
                }
                
            } else {
                // Acción no reconocida, redirigir a GET
                doGet(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("controlador-habitos?action=list&error=exception");
        }
    }
}
