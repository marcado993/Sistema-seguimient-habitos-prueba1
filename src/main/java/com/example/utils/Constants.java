package com.example.utils;

/**
 * Clase de constantes del sistema
 * Centraliza valores constantes utilizados en toda la aplicación
 */
public class Constants {
    
    // Constantes de sesión
    public static final String SESSION_USER_ID = "usuarioId";
    public static final String SESSION_USER_NAME = "usuarioNombre";
    public static final String DEMO_USER = "demo";
    public static final String DEMO_USER_NAME = "Usuario Demo";
    
    // Constantes de límites
    public static final int MAX_NOMBRE_LENGTH = 200;
    public static final int MAX_DESCRIPCION_LENGTH = 1000;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_META_DIARIA = 100;
    
    // Constantes de estados
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_COMPLETADO = "COMPLETADO";
    public static final String ESTADO_PAUSADO = "PAUSADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";
    
    // Constantes de frecuencia
    public static final String FRECUENCIA_DIARIA = "DIARIO";
    public static final String FRECUENCIA_SEMANAL = "SEMANAL";
    public static final String FRECUENCIA_MENSUAL = "MENSUAL";
    
    // Constantes de mensajes
    public static final String MSG_EXITO_CREAR = "Registro creado exitosamente";
    public static final String MSG_EXITO_ACTUALIZAR = "Registro actualizado exitosamente";
    public static final String MSG_EXITO_ELIMINAR = "Registro eliminado exitosamente";
    public static final String MSG_ERROR_GENERAL = "Ocurrió un error al procesar la solicitud";
    public static final String MSG_ERROR_NO_ENCONTRADO = "El registro no fue encontrado";
    public static final String MSG_ERROR_VALIDACION = "Los datos proporcionados no son válidos";
    
    // Constantes de rutas
    public static final String PATH_HABITOS = "/habitos";
    public static final String PATH_OBJETIVOS = "/objetivos";
    public static final String PATH_SEGUIMIENTO = "/seguimiento";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_DASHBOARD = "/dashboard";
    
    // Constructor privado para evitar instanciación
    private Constants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
}
