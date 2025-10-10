package com.example.servlet;

import com.example.dao.UsuarioDAO;
import com.example.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestión de Usuarios
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        List<Usuario> usuarios = usuarioDAO.findAll();
        req.setAttribute("usuarios", usuarios);
        req.getRequestDispatcher("usuarios.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        
        String nombre = req.getParameter("nombre");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        Usuario usuario = new Usuario(nombre, correo, contrasena);
        usuarioDAO.save(usuario);

        resp.sendRedirect("usuarios");
    }
    
    @Override
    public void destroy() {
        super.destroy();
        System.out.println("✓ UsuarioServlet destruido");
    }
}
