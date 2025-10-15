<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificar si el usuario está logueado
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("usuario") == null) {
        // Si no está logueado, redirigir al login
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    // Si está logueado, obtener información del usuario
    String nombreUsuario = (String) currentSession.getAttribute("nombre");
    String correoUsuario = (String) currentSession.getAttribute("correo");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Sistema de Seguimiento de Hábitos</title>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&family=Inter:wght@400;500&family=Dancing+Script:wght@500&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', 'Segoe UI', sans-serif;
            background: #E9F7EF;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .container {
            max-width: 500px;
            background: white;
            border-radius: 16px;
            padding: 3rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            text-align: center;
        }
        
        h1 {
            font-family: 'Poppins', sans-serif;
            color: #555555;
            font-size: 36px;
            font-weight: 700;
            margin-bottom: 10px;
        }
        
        .subtitle {
            font-family: 'Dancing Script', cursive;
            color: #888888;
            font-size: 20px;
            font-weight: 500;
            margin-bottom: 2.5rem;
        }
        
        .menu {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }
        
        .btn {
            padding: 18px;
            border: none;
            border-radius: 16px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: block;
            color: #555555;
        }
        
        .btn-objetivo {
            background: #A8E6CF;
        }
        
        .btn-planificar {
            background: #FFD6A5;
        }
        
        .btn-habito {
            background: #F3E8FF;
        }
        
        .btn-seguimiento {
            background: #FFB6B9;
        }
        
        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
        }
        
        .btn-objetivo:hover {
            background: #90E0BC;
        }
        
        .btn-planificar:hover {
            background: #FFC78C;
        }
        
        .btn-habito:hover {
            background: #E8DAFF;
        }
        
        .btn-seguimiento:hover {
            background: #FFA5A8;
        }
        .user-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 20px;
            border-radius: 12px;
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .user-info .welcome {
            font-size: 14px;
            opacity: 0.9;
        }
        
        .user-info .username {
            font-size: 18px;
            font-weight: 600;
            margin-top: 5px;
        }
        
        .btn-logout {
            background: rgba(255, 255, 255, 0.2);
            color: white;
            padding: 8px 16px;
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 8px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }
        
        .btn-logout:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎯 Sistema de Seguimiento</h1>
        <p class="subtitle">Alcanza tus metas, un hábito a la vez</p>
        
        <!-- Información del usuario -->
        <div class="user-info">
            <div>
                <div class="welcome">👋 Bienvenido/a</div>
                <div class="username"><%= nombreUsuario != null ? nombreUsuario : "Usuario" %></div>
                <div style="font-size: 12px; opacity: 0.8; margin-top: 3px;"><%= correoUsuario %></div>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                🚪 Cerrar Sesión
            </a>
        </div>
        
        <div style="margin-bottom: 2rem; text-align: center;">
            <h2 style="font-family: 'Poppins', sans-serif; color: #555555; font-size: 20px; margin-bottom: 1rem;">🟢 Gestión de Objetivos</h2>
        </div>
        
        <div class="menu" style="margin-bottom: 3rem;">
            <a href="${pageContext.request.contextPath}/controlador-objetivos?action=nuevo" class="btn btn-objetivo">
                <div style="font-size: 24px; margin-bottom: 5px;">📋</div>
                <div>Establecer Objetivo</div>
                <div style="font-size: 12px; opacity: 0.8; margin-top: 5px;">Define tu meta general</div>
            </a>
            <a href="${pageContext.request.contextPath}/controlador-objetivos?action=planificar" class="btn btn-planificar">
                <div style="font-size: 24px; margin-bottom: 5px;">🎯</div>
                <div>Planificar Objetivo</div>
                <div style="font-size: 12px; opacity: 0.8; margin-top: 5px;">Asocia hábitos a tus objetivos</div>
            </a>
        </div>
        
        <div style="margin-bottom: 2rem; text-align: center;">
            <h2 style="font-family: 'Poppins', sans-serif; color: #555555; font-size: 20px; margin-bottom: 1rem;">🟠 Gestión de Hábitos</h2>
        </div>
        
        <div class="menu">
            <a href="${pageContext.request.contextPath}/controlador-habitos?action=list&usuarioId=usuario_demo" class="btn btn-habito">
                <div style="font-size: 24px; margin-bottom: 5px;">📝</div>
                <div>Registrar Hábito</div>
                <div style="font-size: 12px; opacity: 0.8; margin-top: 5px;">Marca si cumpliste hoy</div>
            </a>
            <a href="${pageContext.request.contextPath}/controlador-habitos?action=view&usuarioId=usuario_demo" class="btn btn-seguimiento">
                <div style="font-size: 24px; margin-bottom: 5px;">📊</div>
                <div>Vista de Seguimiento</div>
                <div style="font-size: 12px; opacity: 0.8; margin-top: 5px;">Monitorea tu progreso</div>
            </a>
        </div>
    </div>
</body>
</html>
