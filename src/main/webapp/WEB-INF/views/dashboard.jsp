<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboar        <        <!-- Acciones rápidas -->
        <div style="text-align: center; margin: 30px 0;">
            <a href="habitos?action=new" class="btn">➕ Crear Nuevo Hábito</a>
            <a href="habitos?action=lista" class="btn">📋 Mis Hábitos</a>
            <a href="calendario?action=kanban" class="btn">📅 Calendario Kanban</a>
            <a href="seguimiento?action=calendario" class="btn">📈 Ver Progreso</a>
        </div>iones rápidas -->
        <div class="actions-panel">
            <a href="habitos?action=new" class="btn">➕ Crear Nuevo Hábito</a>
            <a href="habitos?action=list" class="btn">📋 Ver Todos los Hábitos</a>
            <a href="calendario?action=kanban" class="btn">📅 Calendario Kanban</a>
            <a href="seguimiento?action=calendario" class="btn">📊 Ver Progreso</a>
        </div>
        
        <!-- Lista de hábitos del día -->
        <h2 class="section-title">Tus Hábitos</h2>a de Hábitos</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
            background: #F7FAFC;
            color: #2D3748;
            min-height: 100vh;
            padding: 20px;
        }
        
        .container { 
            max-width: 1400px; 
            margin: 0 auto;
        }
        
        .header { 
            background: #FFFFFF;
            padding: 32px;
            border-radius: 16px;
            margin-bottom: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        
        .header:hover {
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
        }
        
        .header h1 {
            color: #2D3748;
            font-size: 32px;
            font-weight: 700;
            letter-spacing: -0.5px;
            margin-bottom: 8px;
        }
        
        .header p {
            color: #718096;
            font-size: 16px;
            font-weight: 500;
        }
        
        .stats { 
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 24px;
        }
        
        .stat-card { 
            background: #FFFFFF;
            padding: 24px;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 20px;
        }
        
        .stat-card:hover {
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
            transform: translateY(-2px);
        }
        
        .stat-icon {
            width: 64px;
            height: 64px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        .stat-icon.primary { background: linear-gradient(135deg, #DBEAFE 0%, #BFDBFE 100%); }
        .stat-icon.success { background: linear-gradient(135deg, #D1FAE5 0%, #A7F3D0 100%); }
        .stat-icon.info { background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%); }
        
        .stat-info h3 {
            font-size: 14px;
            color: #718096;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 4px;
        }
        
        .stat-info p {
            font-size: 36px;
            font-weight: 800;
            color: #2D3748;
            letter-spacing: -1px;
        }
        
        .habit-grid { 
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 20px;
            margin: 24px 0;
        }
        
        .habit-card { 
            background: #FFFFFF;
            padding: 24px;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        
        .habit-card:hover {
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            transform: translateY(-4px);
            border-color: #E2E8F0;
        }
        
        .habit-card h3 {
            font-size: 20px;
            font-weight: 700;
            color: #2D3748;
            margin-bottom: 12px;
            letter-spacing: -0.3px;
        }
        
        .habit-card p {
            color: #718096;
            margin-bottom: 8px;
            line-height: 1.6;
        }
        
        .habit-card strong {
            color: #2D3748;
        }
        
        .btn { 
            padding: 12px 24px;
            background: linear-gradient(135deg, #5A67D8 0%, #667EEA 100%);
            color: white;
            border: none;
            border-radius: 16px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 5px;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(90, 103, 216, 0.3);
        }
        
        .btn:hover { 
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(90, 103, 216, 0.4);
        }
        
        .btn:active {
            transform: translateY(0);
        }
        
        .btn-success { 
            background: linear-gradient(135deg, #48BB78 0%, #38A169 100%);
            box-shadow: 0 2px 8px rgba(72, 187, 120, 0.3);
        }
        
        .btn-success:hover { 
            box-shadow: 0 4px 12px rgba(72, 187, 120, 0.4);
        }
        
        .actions-panel {
            background: #FFFFFF;
            padding: 24px;
            border-radius: 16px;
            margin-bottom: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            text-align: center;
        }
        
        .section-title {
            font-size: 24px;
            font-weight: 700;
            color: #2D3748;
            margin: 32px 0 20px 0;
            letter-spacing: -0.5px;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 40px;
            background: #FFFFFF;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        .empty-state h3 {
            font-size: 24px;
            color: #2D3748;
            margin-bottom: 12px;
        }
        
        .empty-state p {
            color: #718096;
            font-size: 16px;
            margin-bottom: 24px;
        }
        
        .nav-panel {
            background: #FFFFFF;
            padding: 24px;
            border-radius: 16px;
            margin-top: 32px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            text-align: center;
        }
        
        .nav-panel h3 {
            font-size: 20px;
            color: #2D3748;
            margin-bottom: 16px;
            font-weight: 700;
        }
        
        .status-badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 16px;
            font-size: 13px;
            font-weight: 700;
            letter-spacing: 0.3px;
        }
        
        .status-active {
            background: linear-gradient(135deg, #D1FAE5 0%, #A7F3D0 100%);
            color: #48BB78;
        }
        
        .status-inactive {
            background: linear-gradient(135deg, #FEE2E2 0%, #FECACA 100%);
            color: #E53E3E;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🎯 Dashboard de Hábitos</h1>
            <p>Bienvenido a tu sistema de seguimiento personal</p>
        </div>
    
        
        <!-- Estadísticas generales -->
        <div class="stats">
            <div class="stat-card">
                <div class="stat-icon primary">📊</div>
                <div class="stat-info">
                    <h3>Hábitos Activos</h3>
                    <p>${habitosActivos}</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon success">✅</div>
                <div class="stat-info">
                    <h3>Completados Hoy</h3>
                    <p>${habitosCompletadosHoy}</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon info">📅</div>
                <div class="stat-info">
                    <h3>Fecha de Hoy</h3>
                    <p style="font-size: 20px;">${fechaHoy}</p>
                </div>
            </div>
        </div>
        
        <!-- Acciones rápidas -->
        <div style="text-align: center; margin: 30px 0;">
            <a href="habitos?action=new" class="btn">➕ Crear Nuevo Hábito</a>
            <a href="habitos?action=list" class="btn">📋 Ver Todos los Hábitos</a>
            <a href="calendario?action=kanban" class="btn">📅 Calendario Kanban</a>
            <a href="seguimiento?action=calendario" class="btn">� Ver Progreso</a>
        </div>
        
        <!-- Lista de hábitos del día -->
        <h2>Tus Hábitos</h2>
        <div class="habit-grid">
            <c:forEach var="habito" items="${habitos}">
                <div class="habit-card">
                    <h3>📌 ${habito.nombre}</h3>
                    <p>${habito.descripcion}</p>
                    <p><strong>Meta diaria:</strong> ${habito.metaDiaria} veces</p>
                    <p><strong>Frecuencia:</strong> ${habito.frecuencia}</p>
                    <p><strong>Estado:</strong> 
                        <c:choose>
                            <c:when test="${habito.activo}">
                                <span class="status-badge status-active">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge status-inactive">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                    
                    <div style="margin-top: 15px;">
                        <form method="post" action="habitos" style="display: inline;">
                            <input type="hidden" name="action" value="marcar">
                            <input type="hidden" name="habitoId" value="${habito.id}">
                            <button type="submit" class="btn btn-success">✅ Marcar Completado</button>
                        </form>
                        <a href="habitos?action=view&id=${habito.id}" class="btn">👀 Ver Detalles</a>
                        <a href="seguimiento?action=view&habitoId=${habito.id}" class="btn">📊 Seguimiento</a>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty habitos}">
            <div class="empty-state">
                <h3>🏁 ¡Comienza tu viaje de hábitos!</h3>
                <p>No tienes hábitos registrados aún. ¡Crea tu primer hábito para comenzar!</p>
                <a href="habitos?action=new" class="btn btn-success">🚀 Crear Mi Primer Hábito</a>
            </div>
        </c:if>
        
        <!-- Navegación adicional -->
        <div class="nav-panel">
            <h3>🧭 Navegación</h3>
            <a href="habitos?action=lista" class="btn">� Mis Hábitos</a>
            <a href="calendario?action=kanban" class="btn">📅 Calendario Kanban</a>
            <a href="diagnostico.jsp" class="btn">🔧 Diagnóstico</a>
            <a href="login.jsp" class="btn btn-success">🔓 Cerrar Sesión</a>
        </div>
    </div>
</body>
</html>
