<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Hábitos - Sistema de Hábitos</title>
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
            text-align: center;
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
        
        .nav-bar {
            background: #FFFFFF;
            padding: 20px 28px;
            border-radius: 16px;
            margin-bottom: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .nav-buttons {
            display: flex;
            gap: 12px;
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
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(90, 103, 216, 0.3);
        }
        
        .btn:hover { 
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(90, 103, 216, 0.4);
        }
        
        .btn-success { 
            background: linear-gradient(135deg, #48BB78 0%, #38A169 100%);
            box-shadow: 0 2px 8px rgba(72, 187, 120, 0.3);
        }
        
        .btn-success:hover {
            box-shadow: 0 4px 12px rgba(72, 187, 120, 0.4);
        }
        
        .btn-warning { 
            background: linear-gradient(135deg, #F59E0B 0%, #D97706 100%);
            box-shadow: 0 2px 8px rgba(245, 158, 11, 0.3);
        }
        
        .btn-danger { 
            background: linear-gradient(135deg, #E53E3E 0%, #C53030 100%);
            box-shadow: 0 2px 8px rgba(229, 62, 62, 0.3);
        }
        
        .btn-secondary {
            background: #FFFFFF;
            color: #5A67D8;
            border: 2px solid #E2E8F0;
        }
        
        .btn-secondary:hover {
            background: #F7FAFC;
            border-color: #5A67D8;
        }
        
        .table-container {
            background: #FFFFFF;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        .table { 
            width: 100%;
        }
        
        .table th, .table td { 
            padding: 20px;
            text-align: left;
            border-bottom: 1px solid #E2E8F0;
        }
        
        .table th { 
            background: #F7FAFC;
            font-weight: 700;
            color: #2D3748;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .table td {
            color: #2D3748;
            font-size: 14px;
        }
        
        .table tr:hover {
            background: #F7FAFC;
        }
        
        .actions { 
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }
        
        .actions .btn {
            padding: 8px 16px;
            font-size: 12px;
        }
        
        .alert { 
            padding: 20px 24px;
            margin-bottom: 24px;
            border-radius: 16px;
            font-weight: 500;
        }
        
        .alert-success { 
            background: linear-gradient(135deg, #D1FAE5 0%, #A7F3D0 100%);
            color: #065F46;
            border: 2px solid #48BB78;
        }
        
        .alert-error { 
            background: linear-gradient(135deg, #FEE2E2 0%, #FECACA 100%);
            color: #7F1D1D;
            border: 2px solid #E53E3E;
        }
        
        .status-badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 16px;
            font-size: 12px;
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
        
        .empty-state {
            text-align: center;
            padding: 60px 40px;
            background: #FFFFFF;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            margin: 24px 0;
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
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📚 Mis Hábitos</h1>
            <p>Gestiona todos tus hábitos</p>
        </div>
        
        <!-- Mensajes de éxito/error -->
        <c:if test="${param.success != null}">
            <div class="alert alert-success">✅ ${param.success}</div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-error">❌ ${param.error}</div>
        </c:if>
        
        <!-- Navegación -->
        <div class="nav-bar">
            <div class="nav-buttons">
                <a href="habitos?action=dashboard" class="btn btn-secondary">🏠 Dashboard</a>
                <a href="calendario?action=kanban" class="btn btn-secondary">📅 Calendario Kanban</a>
            </div>
            <div class="nav-buttons">
                <a href="habitos?action=new" class="btn btn-success">➕ Crear Nuevo Hábito</a>
                <a href="login.jsp" class="btn btn-danger">🔓 Cerrar Sesión</a>
            </div>
        </div>
        
        <!-- Tabla de hábitos -->
        <c:if test="${not empty habitos}">
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Descripción</th>
                            <th>Frecuencia</th>
                            <th>Meta Diaria</th>
                            <th>Fecha Inicio</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="habito" items="${habitos}">
                            <tr>
                                <td><strong>${habito.nombre}</strong></td>
                                <td>${habito.descripcion}</td>
                                <td>${habito.frecuencia}</td>
                                <td>${habito.metaDiaria} veces</td>
                                <td>${habito.fechaInicio}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${habito.activo}">
                                            <span class="status-badge status-active">✓ Activo</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge status-inactive">✗ Inactivo</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="habitos?action=view&id=${habito.id}" class="btn">👀 Ver</a>
                                        <a href="habitos?action=edit&id=${habito.id}" class="btn btn-warning">✏️ Editar</a>
                                        <a href="seguimiento?action=view&habitoId=${habito.id}" class="btn">📊 Seguimiento</a>
                                        <a href="habitos?action=delete&id=${habito.id}" 
                                           class="btn btn-danger" 
                                           onclick="return confirm('¿Estás seguro de eliminar este hábito?')">🗑️ Eliminar</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        
        <c:if test="${empty habitos}">
            <div class="empty-state">
                <h3>📋 No tienes hábitos registrados</h3>
                <p>¡Comienza creando tu primer hábito para comenzar tu seguimiento!</p>
                <a href="habitos?action=new" class="btn btn-success">🚀 Crear Mi Primer Hábito</a>
            </div>
        </c:if>
    </div>
</body>
</html>
