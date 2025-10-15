package com.example.servlet;

import com.example.dao.UsuarioDAOJPA;
import com.example.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet para manejar el login de usuarios
 */
@WebServlet(name = "ControladorLogin", urlPatterns = {"/login", "/logout", "/registro"})
public class ControladorLogin extends HttpServlet {
    
    private UsuarioDAOJPA usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAOJPA();
        
        // Crear usuario demo si no existe
        if (!usuarioDAO.existsByCorreo("demo@ejemplo.com")) {
            Usuario demo = new Usuario("Usuario Demo", "demo@ejemplo.com", "demo123");
            usuarioDAO.save(demo);
            System.out.println("✓ Usuario demo creado");
        }
        
        System.out.println("✓ ControladorLogin inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/logout":
                cerrarSesion(request, response);
                break;
            case "/registro":
                mostrarRegistro(request, response);
                break;
            default:
                mostrarLogin(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/login":
                procesarLogin(request, response);
                break;
            case "/registro":
                procesarRegistro(request, response);
                break;
            default:
                response.sendRedirect("login.jsp");
                break;
        }
    }
    
    /**
     * Mostrar formulario de login
     */
    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * Procesar login de usuario
     */
    private void procesarLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        
        System.out.println("Intentando login con correo: " + correo);
        
        if (correo == null || correo.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Validar credenciales
        if (usuarioDAO.validarCredenciales(correo, contrasena)) {
            Optional<Usuario> usuarioOpt = usuarioDAO.findByCorreo(correo);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("correo", usuario.getCorreo());
                session.setAttribute("nombre", usuario.getNombre());
                
                System.out.println("✓ Login exitoso para: " + correo);
                
                // Redirigir al dashboard o página principal
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                request.setAttribute("error", "Error al obtener información del usuario");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } else {
            System.out.println("✗ Login fallido para: " + correo);
            request.setAttribute("error", "Correo o contraseña incorrectos");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Mostrar formulario de registro
     */
    private void mostrarRegistro(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }
    
    /**
     * Procesar registro de nuevo usuario
     */
    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");
        
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }
        
        if (!contrasena.equals(confirmarContrasena)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }
        
        // Verificar si el correo ya existe
        Optional<Usuario> existente = usuarioDAO.findByCorreo(correo);
        if (existente.isPresent()) {
            request.setAttribute("error", "El correo ya está registrado");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
            return;
        }
        
        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario(nombre, correo, contrasena);
        usuarioDAO.save(nuevoUsuario);
        
        System.out.println("✓ Usuario registrado: " + correo);
        
        request.setAttribute("mensaje", "Registro exitoso. Por favor inicie sesión.");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * Cerrar sesión
     */
    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("✓ Cerrando sesión para: " + session.getAttribute("correo"));
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
