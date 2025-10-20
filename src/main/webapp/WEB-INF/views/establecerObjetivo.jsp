<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistema_seguimiento.model.Objetivo" %>
<!DOCTYPE html>
<html>
<head>
    <title>Establecer Objetivo - Sistema de Seguimiento</title>
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
            padding: 20px;
            color: #555555;
        }
        
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        h1 {
            font-family: 'Poppins', sans-serif;
            color: #555555;
            margin-bottom: 1.5rem;
            text-align: center;
            font-size: 28px;
            font-weight: 700;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #555555;
            font-weight: 600;
            font-size: 14px;
        }
        
        input[type="text"],
        input[type="date"],
        textarea,
        select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #F3E8FF;
            border-radius: 12px;
            font-size: 14px;
            font-family: 'Inter', sans-serif;
            transition: all 0.3s ease;
            background: #F3E8FF;
        }
        
        input:focus,
        textarea:focus,
        select:focus {
            outline: none;
            border-color: #FFD6A5;
            box-shadow: 0 0 0 3px rgba(255, 214, 165, 0.2);
            background: white;
        }
        
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        
        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 2rem;
        }
        
        button {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 16px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background: #A8E6CF;
            color: #555555;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(168, 230, 207, 0.4);
            background: #90E0BC;
        }
        
        .btn-secondary {
            background: #F3E8FF;
            color: #555555;
        }
        
        .btn-secondary:hover {
            background: #E8DAFF;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(243, 232, 255, 0.4);
        }
        
        /* Breadcrumbs */
        .breadcrumb {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 2rem;
            padding: 1rem;
            background: #F3E8FF;
            border-radius: 12px;
            font-size: 14px;
        }
        
        .breadcrumb-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        .breadcrumb-item.active {
            color: #FFD6A5;
            font-weight: 600;
        }
        
        .breadcrumb-separator {
            color: #888888;
        }
        
        /* Botón Dashboard Header */
        .btn-dashboard {
            padding: 8px 16px;
            background: #FFB6B9;
            color: #555555;
            border: none;
            border-radius: 12px;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-dashboard:hover {
            background: #FFA5A8;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 182, 185, 0.4);
        }
    </style>
</head>
<body>
    <%
        Objetivo objetivo = (Objetivo) request.getAttribute("objetivo");
        Boolean modoEdicion = (Boolean) request.getAttribute("modoEdicion");
        boolean esEdicion = (modoEdicion != null && modoEdicion && objetivo != null);
    %>
    
    <div class="container">
        <!-- Botón Dashboard -->
        <div style="text-align: right; margin-bottom: 1rem;">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn-dashboard">
                🏠 Dashboard
            </a>
        </div>
        
        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <div class="breadcrumb-item active">
                <span>📋</span>
                <span>Paso 1: <%= esEdicion ? "Editar" : "Establecer" %> Objetivo</span>
            </div>
            <span class="breadcrumb-separator">→</span>
            <div class="breadcrumb-item" style="opacity: 0.5;">
                <span>🎯</span>
                <span>Paso 2: Planificar</span>
            </div>
            <span class="breadcrumb-separator">→</span>
            <div class="breadcrumb-item" style="opacity: 0.5;">
                <span>📊</span>
                <span>Paso 3: Seguimiento</span>
            </div>
        </div>
        
        <h1><%= esEdicion ? "✏️ Editar" : "📋 Establecer" %> Objetivo</h1>
        <p style="text-align: center; color: #888888; margin-bottom: 2rem; font-family: 'Dancing Script', cursive; font-size: 18px;">
            <%= esEdicion ? "Modifica los detalles de tu objetivo" : "Define tu meta general para alcanzar" %>
        </p>
        
        <form action="controlador-objetivos" method="post">
            <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "crear" %>">
            <% if (esEdicion) { %>
            <input type="hidden" name="id" value="<%= objetivo.getId() %>">
            <% } %>
            
            <div class="form-group">
                <label for="titulo">Título del Objetivo *</label>
                <input type="text" id="titulo" name="titulo" required 
                       value="<%= esEdicion ? objetivo.getTitulo() : "" %>"
                       placeholder="Ej: Perder 5kg en 3 meses">
            </div>
            
            <div class="form-group">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" 
                          placeholder="Describe tu objetivo en detalle..."><%= esEdicion && objetivo.getDescripcion() != null ? objetivo.getDescripcion() : "" %></textarea>
            </div>
            
            <div class="form-group">
                <label for="fechaInicio">Fecha de Inicio *</label>
                <input type="date" id="fechaInicio" name="fechaInicio" required
                       value="<%= esEdicion && objetivo.getFechaCreacion() != null ? objetivo.getFechaCreacion().toLocalDate() : "" %>">
            </div>
            
            <div class="form-group">
                <label for="fechaFin">Fecha de Finalización *</label>
                <input type="date" id="fechaFin" name="fechaFin" required
                       value="<%= esEdicion && objetivo.getFechaLimite() != null ? objetivo.getFechaLimite().toLocalDate() : "" %>">
            </div>
            
            <div class="form-group">
                <label for="estado">Estado</label>
                <select id="estado" name="estado">
                    <option value="ACTIVO" <%= esEdicion && objetivo.getEstado() == Objetivo.EstadoObjetivo.ACTIVO ? "selected" : "" %>>Activo</option>
                    <option value="PAUSADO" <%= esEdicion && objetivo.getEstado() == Objetivo.EstadoObjetivo.PAUSADO ? "selected" : "" %>>Pausado</option>
                    <option value="COMPLETADO" <%= esEdicion && objetivo.getEstado() == Objetivo.EstadoObjetivo.COMPLETADO ? "selected" : "" %>>Completado</option>
                    <option value="CANCELADO" <%= esEdicion && objetivo.getEstado() == Objetivo.EstadoObjetivo.CANCELADO ? "selected" : "" %>>Cancelado</option>
                </select>
            </div>
            
            <div class="btn-group">
                <% if (esEdicion) { %>
                <button type="button" class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/controlador-objetivos?action=planificar'">
                    ← Cancelar
                </button>
                <% } else { %>
                <button type="button" class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/index.jsp'">
                    🏠 Volver al Dashboard
                </button>
                <% } %>
                <button type="submit" class="btn-primary">
                    ✓ <%= esEdicion ? "Actualizar" : "Crear" %> Objetivo
                </button>
            </div>
        </form>
        
        <div style="text-align: center; margin-top: 1.5rem; padding: 1rem; background: #E9F7EF; border-radius: 12px;">
            <p style="font-size: 14px; color: #888888;">
                💡 <strong>Siguiente paso:</strong> Después de crear tu objetivo, podrás asociarle hábitos específicos
            </p>
        </div>
    </div>
    
    <% if (!esEdicion) { %>
    <script>
        // Solo auto-completar fechas si NO estamos editando
        // Auto-completar fecha de inicio con la fecha actual
        const fechaInicio = document.getElementById('fechaInicio');
        if (!fechaInicio.value) {
            fechaInicio.valueAsDate = new Date();
        }
        
        // Auto-completar fecha de fin (30 días después)
        const fechaFinInput = document.getElementById('fechaFin');
        if (!fechaFinInput.value) {
            const fechaFin = new Date();
            fechaFin.setDate(fechaFin.getDate() + 30);
            fechaFinInput.valueAsDate = fechaFin;
        }
    </script>
    <% } %>
</body>
</html>
