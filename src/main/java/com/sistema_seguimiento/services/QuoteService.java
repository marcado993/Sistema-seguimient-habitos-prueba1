package com.sistema_seguimiento.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para proporcionar frases diarias motivacionales
 * Implementado siguiendo TDD - Fase VERDE
 */
public class QuoteService {
    
    private List<String> quotes;
    
    public QuoteService() {
        this.quotes = new ArrayList<>();
        initializeQuotes();
    }
    
    /**
     * Obtiene la frase diaria
     * @return Una frase motivacional
     */
    public String getDailyQuote() {
        // Usa el día del año para seleccionar una frase diferente cada día
        int dayOfYear = LocalDate.now().getDayOfYear();
        return getDailyQuoteForDay(dayOfYear);
    }
    
    /**
     * Obtiene la frase diaria para un día específico
     * Este método permite simular diferentes días para pruebas
     * @param dayOfYear El día del año (1-365/366)
     * @return Una frase motivacional correspondiente al día
     */
    public String getDailyQuoteForDay(int dayOfYear) {
        int quoteIndex = dayOfYear % quotes.size();
        return quotes.get(quoteIndex);
    }
    
    /**
     * Inicializa la lista de frases motivacionales
     */
    private void initializeQuotes() {
        quotes.add("El éxito es la suma de pequeños esfuerzos repetidos día tras día.");
        quotes.add("No cuentes los días, haz que los días cuenten.");
        quotes.add("La disciplina es el puente entre las metas y los logros.");
        quotes.add("Cada día es una nueva oportunidad para mejorar.");
        quotes.add("Los hábitos son la base del éxito a largo plazo.");
        quotes.add("La constancia es la clave del progreso.");
        quotes.add("Hoy es el día perfecto para comenzar.");
        quotes.add("Pequeños pasos cada día llevan a grandes cambios.");
        quotes.add("Tu futuro se crea con lo que haces hoy, no mañana.");
        quotes.add("La motivación te inicia, el hábito te mantiene.");
        quotes.add("Cree en ti mismo y todo será posible.");
        quotes.add("El único modo de hacer un gran trabajo es amar lo que haces.");
        quotes.add("No esperes el momento perfecto, toma el momento y hazlo perfecto.");
        quotes.add("El éxito no es el final, el fracaso no es fatal: es el coraje para continuar lo que cuenta.");
        quotes.add("Tu única limitación es la que te impones a ti mismo.");
    }
}
