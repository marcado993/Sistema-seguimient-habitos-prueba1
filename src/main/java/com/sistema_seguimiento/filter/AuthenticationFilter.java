package com.sistema_seguimiento.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro de autenticación para proteger páginas que requieren login
 * Redirige a login si el usuario no está autenticado
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
    "/index.jsp",
    "/controlador-objetivos",
    "/controlador-habitos",
    "/planificar"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("✓ AuthenticationFilter inicializado");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Obtener la sesión (sin crear una nueva si no existe)
        HttpSession session = httpRequest.getSession(false);
        
        // Verificar si el usuario está logueado
        boolean isLoggedIn = (session != null && session.getAttribute("usuario") != null);
        
        // Obtener la URI solicitada
        String requestURI = httpRequest.getRequestURI();
        
        if (isLoggedIn) {
            // Usuario autenticado, permitir acceso
            chain.doFilter(request, response);
        } else {
            // Usuario no autenticado, redirigir a login
            System.out.println("⚠️ Acceso denegado a: " + requestURI + " - Usuario no autenticado");
            
            // Guardar la URL original para redirigir después del login
            session = httpRequest.getSession();
            session.setAttribute("redirectAfterLogin", requestURI);
            
            // Redirigir a la página de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        System.out.println("✓ AuthenticationFilter destruido");
    }
}
