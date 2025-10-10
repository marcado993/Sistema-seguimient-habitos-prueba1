<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${habito.nombre} - Detalle del Hábito</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .header { background: #2196F3; color: white; padding: 20px; text-align: center; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
        .btn { padding: 10px 20px; background: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn:hover { background: #1976D2; }
        .btn-success { background: #4CAF50; }
        .btn-warning { background: #FF9800; }
        .btn-danger { background: #f44336; }
        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin: 20px 0; }
        .info-item { padding: 15px; background: #f9f9f9; border-radius: 4px; }
        .info-item strong { color: #2196F3; }
        .progress-section { margin: 30px 0; }
        .progress-bar { width: 100%; height: 25px; background: #e0e0e0; border-radius: 12px; overflow: hidden; margin: 10px 0; }
        .progress { height: 100%; background: #4CAF50; transition: width 0.3s; }
        .streak { font-size: 2em; color: #FF5722; text-align: center; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="header">
        <h1>${habito.nombre}</h1>
        <p>Información detallada de tu hábito</p>
    </div>
    
    <div class="container">
        <!-- Navegación -->
        <div style="text-align: center; margin: 20px 0;">
            <a href="habitos?action=list" class="btn">Volver a la Lista</a>
            <a href="habitos?action=edit&id=${habito.id}" class="btn btn-warning">Editar</a>
            <a href="seguimiento?action=view&habitoId=${habito.id}" class="btn">Ver Seguimiento</a>
            <a href="habitos?action=dashboard" class="btn">Dashboard</a>
        </div>
        
        <!-- Información básica -->
        <div class="card">
            <h2>Información del Hábito</h2>
            <div class="info-grid">
                <div class="info-item">
                    <strong>Nombre:</strong><br>
                    ${habito.nombre}
                </div>
                <div class="info-item">
                    <strong>Estado:</strong><br>
                    <c:choose>
                        <c:when test="${habito.activo}">
                            <span style="color: #4CAF50;">Activo</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #f44336;">Inactivo</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="info-item">
                    <strong>Frecuencia:</strong><br>
                    ${habito.frecuencia}
                </div>
                <div class="info-item">
                    <strong>Meta Diaria:</strong><br>
                    ${habito.metaDiaria} veces por día
                </div>
                <div class="info-item">
                    <strong>Fecha de Inicio:</strong><br>
                    ${habito.fechaInicio}
                </div>
                <div class="info-item">
                    <strong>Fecha de Creación:</strong><br>
                    ${habito.fechaCreacion}
                </div>
            </div>
            
            <c:if test="${not empty habito.descripcion}">
                <div style="margin: 20px 0;">
                    <strong>Descripción:</strong><br>
                    <p style="background: #f9f9f9; padding: 15px; border-radius: 4px; margin: 10px 0;">
                        ${habito.descripcion}
                    </p>
                </div>
            </c:if>
        </div>
        
        <!-- Estadísticas de progreso -->
        <div class="card">
            <h2>Progreso y Estadísticas</h2>
            
            <!-- Racha actual -->
            <div style="text-align: center; background: #f9f9f9; padding: 20px; border-radius: 4px; margin: 20px 0;">
                <h3>Racha Actual</h3>
                <div class="streak">0 días</div>
                <p>¡Comienza tu racha marcando el hábito como completado!</p>
            </div>
            
            <!-- Progreso del mes actual -->
            <div class="progress-section">
                <h3>Progreso del Mes Actual</h3>
                <c:set var="porcentajeMes" value="0"/>
                
                <div class="progress-bar">
                    <div class="progress" style="width: ${porcentajeMes}%"></div>
                </div>
                <p>Progreso del mes: ${porcentajeMes}% completado</p>
            </div>
        </div>
        
        <!-- Acciones rápidas -->
        <div class="card">
            <h2>Acciones</h2>
            <div style="text-align: center;">
                <form method="post" action="habitos" style="display: inline; margin-right: 10px;">
                    <input type="hidden" name="action" value="marcar">
                    <input type="hidden" name="habitoId" value="${habito.id}">
                    <button type="submit" class="btn btn-success">Marcar como Completado Hoy</button>
                </form>
                
                <a href="seguimiento?action=view&habitoId=${habito.id}" class="btn">Ver Historial Completo</a>
                
                <a href="habitos?action=delete&id=${habito.id}" 
                   class="btn btn-danger" 
                   onclick="return confirm('¿Estás seguro de eliminar este hábito?')">
                   Eliminar Hábito
                </a>
            </div>
        </div>
        
        <!-- Información de seguimiento -->
        <div class="card">
            <h2>Últimos Seguimientos</h2>
            <c:choose>
                <c:when test="${not empty habito.seguimientos}">
                    <div style="max-height: 300px; overflow-y: auto;">
                        <c:forEach var="seguimiento" items="${habito.seguimientos}" end="9">
                            <div style="border-bottom: 1px solid #eee; padding: 10px 0;">
                                <strong>${seguimiento.fecha}</strong>
                                - Completado ${seguimiento.completado} veces
                                <c:if test="${not empty seguimiento.notas}">
                                    <br><em>Nota: ${seguimiento.notas}</em>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; color: #666; padding: 20px;">
                        Aún no tienes registros de seguimiento para este hábito.
                        ¡Comienza marcándolo como completado!
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
