package com.sistema_seguimiento.dao;

import java.util.List;

/**
 * Interfaz DAO para acceso a frases motivacionales
 * Define el contrato para obtener frases desde una fuente de datos
 */
public interface QuoteDAO {
    
    /**
     * Obtiene la lista completa de frases motivacionales
     * @return Lista de frases como cadenas de texto
     */
    List<String> getQuotes();
}
