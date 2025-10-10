<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seguimiento - ${habito.nombre}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .container { max-width: 900px; margin: 0 auto; padding: 20px; }
        .header { background: #2196F3; color: white; padding: 20px; text-align: center; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
        .btn { padding: 10px 20px; background: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn:hover { background: #1976D2; }
        .btn-success { background: #4CAF50; }
        .form-inline { display: flex; gap: 10px; align-items: center; margin: 20px 0; }
        .form-inline input, .form-inline select { padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .status-today { padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0; }
        .status-completed { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .status-pending { background: #fff3cd; color: #856404; border: 1px solid #ffeaa7; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Seguimiento: ${habito.nombre}</h1>
        <p>Registra y monitorea tu progreso</p>
    </div>
    
    <div class="container">
        <!-- Navegación -->
        <div style="text-align: center; margin: 20px 0;">
            <a href="habitos?action=view&id=${habito.id}" class="btn">Volver al Hábito</a>
            <a href="habitos?action=list" class="btn">Lista de Hábitos</a>
            <a href="seguimiento?action=calendario" class="btn">Ver Calendario</a>
            <a href="habitos?action=dashboard" class="btn">Dashboard</a>
        </div>
        
        <!-- Estado de hoy -->
        <div class="card">
            <h2>Estado de Hoy - ${fechaActual}</h2>
            
            <c:choose>
                <c:when test="${seguimientoHoy != null && seguimientoHoy.completado >= habito.metaDiaria}">
                    <div class="status-today status-completed">
                        <h3>¡Excelente! Meta completada</h3>
                        <p>Has realizado ${seguimientoHoy.completado} veces tu hábito hoy (meta: ${habito.metaDiaria})</p>
                        <c:if test="${not empty seguimientoHoy.notas}">
                            <p><strong>Notas:</strong> ${seguimientoHoy.notas}</p>
                        </c:if>
                    </div>
                </c:when>
                <c:when test="${seguimientoHoy != null}">
                    <div class="status-today status-pending">
                        <h3>En progreso</h3>
                        <p>Has realizado ${seguimientoHoy.completado} de ${habito.metaDiaria} veces tu hábito hoy</p>
                        <c:if test="${not empty seguimientoHoy.notas}">
                            <p><strong>Notas:</strong> ${seguimientoHoy.notas}</p>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="status-today status-pending">
                        <h3>Aún no has comenzado hoy</h3>
                        <p>Meta diaria: ${habito.metaDiaria} veces</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Formulario para marcar progreso -->
        <div class="card">
            <h2>Registrar Progreso</h2>
            <form method="post" action="seguimiento">
                <input type="hidden" name="action" value="marcar">
                <input type="hidden" name="habitoId" value="${habito.id}">
                <input type="hidden" name="redirect" value="seguimiento?action=view&habitoId=${habito.id}">
                
                <div class="form-inline">
                    <label>Fecha:</label>
                    <input type="date" name="fecha" value="${fechaActual}" required>
                    
                    <label>Notas:</label>
                    <input type="text" name="notas" placeholder="¿Cómo te sentiste? ¿Alguna observación?">
                    
                    <button type="submit" class="btn btn-success">Marcar Completado</button>
                </div>
            </form>
        </div>
        
        <!-- Actualizar seguimiento existente -->
        <c:if test="${seguimientoHoy != null}">
            <div class="card">
                <h2>Actualizar Registro de Hoy</h2>
                <form method="post" action="seguimiento">
                    <input type="hidden" name="action" value="actualizar">
                    <input type="hidden" name="habitoId" value="${habito.id}">
                    <input type="hidden" name="fecha" value="${fechaActual}">
                    
                    <div class="form-inline">
                        <label>Veces completadas:</label>
                        <input type="number" name="completado" value="${seguimientoHoy.completado}" min="0" max="50" required>
                        
                        <label>Notas:</label>
                        <input type="text" name="notas" value="${seguimientoHoy.notas}" placeholder="Actualiza tus notas">
                        
                        <button type="submit" class="btn">Actualizar</button>
                    </div>
                </form>
            </div>
        </c:if>
        
        <!-- Resumen semanal -->
        <div class="card">
            <h2>Resumen de los Últimos 7 Días</h2>
            <div style="display: grid; grid-template-columns: repeat(7, 1fr); gap: 10px; text-align: center;">
                <c:forEach var="i" begin="6" end="0" step="-1">
                    <jsp:useBean id="calendar" class="java.util.GregorianCalendar"/>
                    <jsp:useBean id="dateFormat" class="java.text.SimpleDateFormat"/>
                    <c:set target="${dateFormat}" property="pattern" value="dd/MM"/>
                    
                    <!-- Aquí iría la lógica para calcular las fechas de los últimos 7 días -->
                    <div style="padding: 15px; border-radius: 4px; background: #f9f9f9;">
                        <div style="font-weight: bold;">Día ${7-i}</div>
                        <div style="color: #666;">--/--</div>
                        <div style="margin-top: 5px;">
                            <!-- Aquí mostrarías el estado de ese día -->
                            <span style="color: #4CAF50;">✓</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <!-- Información del hábito -->
        <div class="card">
            <h2>Información del Hábito</h2>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div>
                    <strong>Nombre:</strong> ${habito.nombre}<br>
                    <strong>Frecuencia:</strong> ${habito.frecuencia}<br>
                    <strong>Meta diaria:</strong> ${habito.metaDiaria} veces
                </div>
                <div>
                    <strong>Fecha inicio:</strong> ${habito.fechaInicio}<br>
                    <strong>Días consecutivos:</strong> ${habito.diasConsecutivos}<br>
                    <strong>Estado:</strong> ${habito.activo ? 'Activo' : 'Inactivo'}
                </div>
            </div>
            
            <c:if test="${not empty habito.descripcion}">
                <div style="margin-top: 15px;">
                    <strong>Descripción:</strong><br>
                    <p style="background: #f9f9f9; padding: 10px; border-radius: 4px; margin: 5px 0;">
                        ${habito.descripcion}
                    </p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
