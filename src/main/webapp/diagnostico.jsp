<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Diagnóstico del Sistema</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; }
        .status { padding: 10px; margin: 10px 0; border-radius: 4px; }
        .success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .info { background: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }
        .btn { padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; display: inline-block; margin: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔧 Diagnóstico del Sistema de Hábitos</h1>
        
        <div class="status success">
            <strong>✅ Servidor funcionando:</strong> Tomcat está ejecutándose correctamente
        </div>
        
        <div class="status success">
            <strong>✅ JSP funcionando:</strong> Esta página se está renderizando correctamente
        </div>
        
        <div class="status info">
            <strong>📋 Información del servidor:</strong><br>
            Servidor: <%= application.getServerInfo() %><br>
            Versión Java: <%= System.getProperty("java.version") %><br>
            Context Path: <%= request.getContextPath() %><br>
            Session ID: <%= session.getId() %>
        </div>
        
        <div class="status info">
            <strong>🌐 URLs de prueba:</strong><br>
            Contexto actual: <%= request.getContextPath() %><br>
            URL base: <%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>
        </div>
        
        <h2>🧪 Pruebas del Sistema</h2>
        
        <div style="margin: 20px 0;">
            <a href="<%= request.getContextPath() %>/habitos?action=dashboard" class="btn">🎯 Ir al Dashboard</a>
            <a href="<%= request.getContextPath() %>/habitos?action=list" class="btn">📋 Ver Lista de Hábitos</a>
            <a href="<%= request.getContextPath() %>/habitos?action=new" class="btn">➕ Crear Nuevo Hábito</a>
            <a href="<%= request.getContextPath() %>/login.jsp" class="btn">🔐 Página de Login</a>
        </div>
        
        <div class="status info">
            <strong>💡 Si ves errores 404:</strong><br>
            1. Verifica que el proyecto esté desplegado correctamente<br>
            2. Asegúrate de que las clases Java estén compiladas<br>
            3. Verifica que el contexto de la aplicación sea correcto<br>
            4. Revisa que Tomcat tenga todas las dependencias necesarias
        </div>
        
        <p><strong>Fecha/Hora:</strong> <%= new Date() %></p>
    </div>
</body>
</html>
