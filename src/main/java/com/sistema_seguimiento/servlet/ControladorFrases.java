package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.services.QuoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para gestionar las frases motivacionales
 */
@WebServlet(name = "ControladorFrases", urlPatterns = {"/frases-motivacion"})
public class ControladorFrases extends HttpServlet {
    
    private QuoteService quoteService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        quoteService = new QuoteService();
        System.out.println("Controlador de Frases Motivacionales inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("siguiente".equals(action)) {
            // Obtener el índice actual o inicializarlo
            Integer indiceActual = (Integer) session.getAttribute("indiceFrase");
            if (indiceActual == null) {
                indiceActual = 0;
            } else {
                indiceActual = (indiceActual + 1) % 15; // Hay 15 frases
            }
            session.setAttribute("indiceFrase", indiceActual);
            
            // Obtener la frase del día correspondiente al índice
            String frase = quoteService.getDailyQuoteForDay(indiceActual + 1);
            request.setAttribute("fraseActual", frase);
            request.setAttribute("numeroFrase", indiceActual + 1);
        } else {
            // Primera carga - mostrar frase del día actual
            String frase = quoteService.getDailyQuote();
            Integer indice = (Integer) session.getAttribute("indiceFrase");
            if (indice == null) {
                indice = 0;
                session.setAttribute("indiceFrase", indice);
            }
            request.setAttribute("fraseActual", frase);
            request.setAttribute("numeroFrase", indice + 1);
        }
        
        // Forward a la página JSP
        request.getRequestDispatcher("/WEB-INF/views/fraseMotivacion.jsp").forward(request, response);
    }
}
