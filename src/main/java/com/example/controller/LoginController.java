package com.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        
        // Validación simple para demo
        if ("demo".equals(usuario) && "demo123".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioId", usuario);
            session.setAttribute("usuarioNombre", "Usuario Demo");
            
            // Redirigir al dashboard
            response.sendRedirect("habitos?action=dashboard");
        } else {
            // Login fallido, volver al login con error
            response.sendRedirect("login.jsp?error=Credenciales incorrectas");
        }
    }
}
