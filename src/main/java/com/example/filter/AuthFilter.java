package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Permitir acceso a recursos estáticos y páginas públicas
        if (path.equals("/") || path.equals("") || path.equals("/index.jsp") || 
            path.equals("/login.jsp") || path.startsWith("/login") ||
            path.startsWith("/css/") || path.startsWith("/js/") || 
            path.startsWith("/images/") || path.endsWith(".css") || 
            path.endsWith(".js") || path.endsWith(".png") || path.endsWith(".jpg")) {
            
            chain.doFilter(request, response);
            return;
        }
        
        // Para todas las demás rutas, verificar o crear sesión
        HttpSession session = httpRequest.getSession(false);
        String usuarioId = null;
        
        if (session != null) {
            usuarioId = (String) session.getAttribute("usuarioId");
        }
        
        // Si no hay sesión, crear una sesión demo automáticamente
        if (usuarioId == null) {
            session = httpRequest.getSession(true);
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
        }
        
        chain.doFilter(request, response);
    }
}
