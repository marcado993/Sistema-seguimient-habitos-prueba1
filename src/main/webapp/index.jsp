<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificar si el usuario está logueado
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("usuario") == null) {
        // Si no         <!-- Sección Hábitos -->
        <div class="section-title" style="margin-top: 2rem;">
            📝 Gestión de Hábitos
        </div>
        <div class="section-divider"></div>logueado, redirigir al login
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
    <title>Dashboard - Sistema de Seguimiento</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            padding: 20px;
            color: #555555;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        /* Header */
        .header {
            background: white;
            padding: 2rem;
            border-radius: 16px;
            margin-bottom: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .header-left h1 {
            font-family: 'Poppins', sans-serif;
            color: #555555;
            font-size: 32px;
            font-weight: 700;
            margin-bottom: 5px;
        }
        
        .header-left .subtitle {
            font-family: 'Dancing Script', cursive;
            color: #888888;
            font-size: 18px;
        }
        
        .user-info {
            background: linear-gradient(135deg, #A8E6CF 0%, #FFD6A5 100%);
            color: #555555;
            padding: 15px 20px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .user-avatar {
            width: 50px;
            height: 50px;
            background: rgba(255, 255, 255, 0.6);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
        }
        
        .user-details .welcome {
            font-size: 12px;
            opacity: 0.8;
        }
        
        .user-details .username {
            font-size: 16px;
            font-weight: 600;
            margin-top: 2px;
        }
        
        .user-details .email {
            font-size: 11px;
            opacity: 0.7;
            margin-top: 2px;
        }
        
        .btn-logout {
            background: rgba(255, 255, 255, 0.6);
            color: #555555;
            padding: 8px 16px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-logout:hover {
            background: white;
            transform: translateY(-2px);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        
        /* Dashboard Grid */
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .dashboard-card {
            background: white;
            padding: 2rem;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
            text-decoration: none;
            color: #555555;
            display: block;
            position: relative;
            overflow: hidden;
        }
        
        .dashboard-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #A8E6CF, #FFD6A5);
            transform: scaleX(0);
            transition: transform 0.3s ease;
        }
        
        .dashboard-card:hover::before {
            transform: scaleX(1);
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
        }
        
        .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            margin-bottom: 1rem;
        }
        
        .card-objetivo .card-icon { background: #A8E6CF; }
        .card-planificar .card-icon { background: #FFD6A5; }
        .card-habito .card-icon { background: #F3E8FF; }
        .card-seguimiento .card-icon { background: #FFB6B9; }
        
        .card-title {
            font-family: 'Poppins', sans-serif;
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 8px;
            color: #555555;
        }
        
        .card-description {
            font-size: 14px;
            color: #888888;
            line-height: 1.6;
        }
        
        /* Section Titles */
        .section-title {
            font-family: 'Poppins', sans-serif;
            font-size: 24px;
            font-weight: 600;
            color: #555555;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .section-divider {
            height: 3px;
            background: linear-gradient(90deg, #A8E6CF, transparent);
            margin-bottom: 1.5rem;
            border-radius: 2px;
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            .header {
                flex-direction: column;
                text-align: center;
            }
            
            .user-info {
                flex-direction: column;
            }
            
            .dashboard-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header con info del usuario -->
        <div class="header">
            <div class="header-left">
                <h1>🎯 Dashboard - Sistema de Seguimiento</h1>
                <p class="subtitle">Alcanza tus metas, un hábito a la vez</p>
            </div>
            <div class="user-info">
                <div class="user-avatar">👤</div>
                <div class="user-details">
                    <div class="welcome">Bienvenido/a</div>
                    <div class="username"><%= nombreUsuario != null ? nombreUsuario : "Usuario" %></div>
                    <div class="email"><%= correoUsuario %></div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                    🚪 Salir
                </a>
            </div>
        </div>
        
        <!-- Sección Objetivos -->
        <div class="section-title">
            🎯 Gestión de Objetivos
        </div>
        <div class="section-divider"></div>
        
        <div class="dashboard-grid">
            <a href="${pageContext.request.contextPath}/controlador-objetivos?action=nuevo" class="dashboard-card card-objetivo">
                <div class="card-icon">📋</div>
                <div class="card-title">Establecer Objetivo</div>
                <div class="card-description">
                    Define tu meta general y establece fechas límite para alcanzarla. Este es el primer paso hacia el éxito.
                </div>
            </a>
            
            <a href="${pageContext.request.contextPath}/controlador-objetivos?action=planificar" class="dashboard-card card-planificar">
                <div class="card-icon">🎯</div>
                <div class="card-title">Planificar Objetivo</div>
                <div class="card-description">
                    Asocia hábitos específicos a tus objetivos para crear un plan de acción efectivo y medible.
                </div>
            </a>
        </div>
        
        <!-- Sección Hábitos -->
        <div class="section-title" style="margin-top: 2rem;">
            � Gestión de Hábitos
        </div>
        <div class="section-divider"></div>
        
        <div class="dashboard-grid">
            <a href="${pageContext.request.contextPath}/controlador-habitos?action=list&usuarioId=usuario_demo" class="dashboard-card card-habito">
                <div class="card-icon">✅</div>
                <div class="card-title">Registrar Hábito Diario</div>
                <div class="card-description">
                    Marca tus hábitos cumplidos del día. La consistencia es la clave del éxito en la formación de hábitos.
                </div>
            </a>
            
            <a href="${pageContext.request.contextPath}/controlador-habitos?action=view&usuarioId=usuario_demo" class="dashboard-card card-seguimiento">
                <div class="card-icon">📊</div>
                <div class="card-title">Vista de Seguimiento</div>
                <div class="card-description">
                    Monitorea tu progreso con estadísticas detalladas. Visualiza rachas, porcentajes de éxito y más.
                </div>
            </a>
        </div>
        
        <!-- Footer Info -->
        <div style="text-align: center; margin-top: 3rem; padding: 2rem; background: white; border-radius: 16px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);">
            <p style="font-family: 'Dancing Script', cursive; font-size: 20px; color: #555555; margin-bottom: 10px;">
                "El éxito es la suma de pequeños esfuerzos repetidos día tras día"
            </p>
            <p style="font-size: 12px; color: #888888;">
                Sistema de Seguimiento de Hábitos y Objetivos © 2025
            </p>
        </div>
    </div>
</body>
</html>
