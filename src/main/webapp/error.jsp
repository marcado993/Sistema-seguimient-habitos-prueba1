<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Sistema de Hábitos</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0; 
            background: #f5f5f5;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }
        .error-container { 
            background: white; 
            padding: 40px; 
            border-radius: 10px; 
            box-shadow: 0 4px 20px rgba(0,0,0,0.1); 
            text-align: center;
            max-width: 500px;
        }
        .error-icon {
            font-size: 4em;
            color: #f44336;
            margin-bottom: 20px;
        }
        .btn { 
            padding: 12px 24px; 
            background: #2196F3; 
            color: white; 
            border: none; 
            border-radius: 5px; 
            cursor: pointer; 
            text-decoration: none;
            display: inline-block;
            margin: 10px;
        }
        .btn:hover { 
            background: #1976D2; 
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1>¡Oops! Algo salió mal</h1>
        
        <c:choose>
            <c:when test="${not empty error}">
                <p style="color: #f44336; font-weight: bold;">${error}</p>
            </c:when>
            <c:otherwise>
                <p>Ha ocurrido un error inesperado en el sistema.</p>
            </c:otherwise>
        </c:choose>
        
        <p style="color: #666;">
            No te preocupes, puedes intentar nuevamente o volver al inicio.
        </p>
        
        <div style="margin-top: 30px;">
            <a href="javascript:history.back()" class="btn">Volver Atrás</a>
            <a href="habitos?action=dashboard" class="btn">Ir al Dashboard</a>
            <a href="habitos?action=list" class="btn">Ver Hábitos</a>
        </div>
        
        <div style="margin-top: 20px; font-size: 12px; color: #999;">
            Si el problema persiste, por favor contacta al administrador del sistema.
        </div>
    </div>
</body>
</html>
