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
        
        if ("nuevo".equals(action)) {
            // Mostrar formulario para crear objetivo
            request.getRequestDispatcher("/WEB-INF/views/objetivos/formulario.jsp").forward(request, response);
        } else if ("listar".equals(action)) {
            // Listar objetivos del usuario
            request.setAttribute("objetivos", objetivoDAO.findByUsuarioId(usuarioId));
            request.getRequestDispatcher("/WEB-INF/views/objetivos/lista.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        String action = request.getParameter("action");
        
        if ("crear".equals(action)) {
            String nombre = request.getParameter("nombre");
            String categoria = request.getParameter("categoria");
            String frecuencia = request.getParameter("frecuencia");
            
            Objetivo nuevoObjetivo = crear(nombre, categoria, frecuencia);
            
            if (nuevoObjetivo != null) {
                notificarExitoRegistro(nuevoObjetivo);
                limpiarFormulario(request);
                response.sendRedirect("controlador-objetivos?action=listar&success=true");
            } else {
                response.sendRedirect("controlador-objetivos?action=nuevo&error=true");
            }
        }
    }
}
