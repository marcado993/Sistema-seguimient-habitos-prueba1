<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Objetivo" %>
<%@ page import="com.example.model.Habito" %>
<!DOCTYPE html>
<html>
<head>
    <title>Planificar Objetivo - Sistema de Seguimiento</title>
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
            max-width: 900px;
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
        
        .planificacion-section {
            background: #F3E8FF;
            padding: 1.5rem;
            border-radius: 16px;
            margin-bottom: 1.5rem;
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
        input[type="number"],
        input[type="date"],
        textarea,
        select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 12px;
            font-size: 14px;
            font-family: 'Inter', sans-serif;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.6);
        }
        
        input:focus,
        textarea:focus,
        select:focus {
            outline: none;
            border-color: #FFD6A5;
            box-shadow: 0 0 0 3px rgba(255, 214, 165, 0.2);
            background: white;
        }
        
        .estrategia-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 25px;
        }
        
        .frecuencia-options {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .frecuencia-option {
            flex: 1;
            min-width: 120px;
            padding: 12px;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 12px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.4);
        }
        
        .frecuencia-option:hover {
            border-color: #FFD6A5;
            background: white;
        }
        
        .frecuencia-option input[type="radio"] {
            display: none;
        }
        
        .frecuencia-option input[type="radio"]:checked + label {
            color: #555555;
            font-weight: 700;
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
        
        /* Cards de objetivos */
        .objetivos-grid {
            display: grid;
            gap: 20px;
            margin-bottom: 2rem;
        }
        
        .objetivo-card {
            background: linear-gradient(135deg, #FFD6A5 0%, #FFC78C 100%);
            padding: 1.5rem;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
            cursor: pointer;
            border: 3px solid transparent;
            position: relative;
        }
        
        .objetivo-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
        }
        
        .objetivo-card.selected {
            border-color: #A8E6CF;
            box-shadow: 0 6px 20px rgba(168, 230, 207, 0.4);
        }
        
        .objetivo-card h3 {
            font-family: 'Poppins', sans-serif;
            color: #555555;
            font-size: 20px;
            margin-bottom: 10px;
            padding-right: 80px;
        }
        
        .objetivo-card p {
            color: #555555;
            font-size: 14px;
            line-height: 1.5;
        }
        
        .objetivo-meta {
            display: flex;
            justify-content: space-between;
            margin-top: 10px;
            padding-top: 10px;
            border-top: 2px solid rgba(255, 255, 255, 0.5);
        }
        
        .card-actions {
            position: absolute;
            top: 15px;
            right: 15px;
            display: flex;
            gap: 8px;
        }
        
        .btn-icon {
            width: 32px;
            height: 32px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.6);
        }
        
        .btn-icon:hover {
            transform: scale(1.1);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
        }
        
        .btn-edit {
            color: #555555;
        }
        
        .btn-edit:hover {
            background: #A8E6CF;
        }
        
        .btn-delete {
            color: #555555;
        }
        
        .btn-delete:hover {
            background: #FF6B6B;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎯 Planificar Objetivo</h1>
        
        <%
            List<Objetivo> objetivos = (List<Objetivo>) request.getAttribute("objetivos");
            Long objetivoRecienCreado = (Long) request.getAttribute("objetivoRecienCreado");
            String mensaje = (String) session.getAttribute("mensaje");
            
            // Limpiar mensaje de sesión
            if (mensaje != null) {
                session.removeAttribute("mensaje");
            }
            
            if (objetivos != null && !objetivos.isEmpty()) {
        %>
        
        <% if (mensaje != null) { %>
        <div style="background: #A8E6CF; color: #555555; padding: 1rem; border-radius: 12px; margin-bottom: 1.5rem; text-align: center;">
            <%= mensaje %>
        </div>
        <% } %>
        
        <div style="text-align: center; margin-bottom: 2rem;">
            <p style="font-family: 'Dancing Script', cursive; font-size: 18px; color: #888888;">
                Selecciona el objetivo que deseas planificar
            </p>
            <p style="color: #888888; font-size: 14px; margin-top: 0.5rem;">
                Total de objetivos: <strong><%= objetivos.size() %></strong>
            </p>
        </div>
        
        <div class="objetivos-grid">
            <% 
            for (Objetivo objetivo : objetivos) { 
                boolean esNuevo = objetivoRecienCreado != null && objetivo.getId().equals(objetivoRecienCreado);
                String claseExtra = esNuevo ? " selected" : "";
                Long objId = objetivo.getId();
            %>
            <div class="objetivo-card<%= claseExtra %>" data-objetivo-id="<%= objId %>">
                <div class="card-actions">
                    <button class="btn-icon btn-edit" data-id="<%= objId %>" title="Editar objetivo">
                        ✏️
                    </button>
                    <button class="btn-icon btn-delete" data-id="<%= objId %>" data-titulo="<%= objetivo.getTitulo() %>" title="Eliminar objetivo">
                        🗑️
                    </button>
                </div>
                <% if (esNuevo) { %>
                <div style="background: #A8E6CF; color: #555555; padding: 5px 10px; border-radius: 8px; font-size: 12px; font-weight: 600; margin-bottom: 10px; display: inline-block;">
                    ✨ Recién creado
                </div>
                <% } %>
                <h3>🎯 <%= objetivo.getTitulo() %></h3>
                <p><%= objetivo.getDescripcion() != null ? objetivo.getDescripcion() : "Sin descripción" %></p>
                <div class="objetivo-meta">
                    <span>📅 <%= objetivo.getFechaCreacion().toLocalDate() %></span>
                    <span>⏰ <%= objetivo.getFechaLimite() != null ? objetivo.getFechaLimite().toLocalDate() : "Sin límite" %></span>
                </div>
            </div>
            <% } %>
        </div>
        
        <% } else { %>
        
        <div style="text-align: center; padding: 3rem; background: #F3E8FF; border-radius: 16px; margin-bottom: 2rem;">
            <div style="font-size: 64px; margin-bottom: 1rem;">📭</div>
            <h3 style="color: #555555; margin-bottom: 1rem;">No tienes objetivos establecidos</h3>
            <p style="color: #888888;">Primero debes establecer un objetivo para poder planificarlo</p>
            <a href="${pageContext.request.contextPath}/controlador-objetivos?action=nuevo" class="btn-primary" style="display: inline-block; margin-top: 1.5rem; text-decoration: none;">
                ➕ Establecer Objetivo
            </a>
        </div>
        
        <% } %>
        
        <div id="formulario-habito" style="display: none;">
        
        <form action="controlador-habitos" method="post" id="form-habito">
            <input type="hidden" name="action" value="crear-con-objetivo">
            <input type="hidden" name="objetivoId" id="objetivoId">
            
            <div class="planificacion-section">
                <h2 style="color: #38b2ac; margin-bottom: 20px;">🎯 Crear Hábito Asociado</h2>
                
                <div class="estrategia-grid">
                    <div class="form-group">
                        <label for="nombre">Nombre del Hábito *</label>
                        <input type="text" id="nombre" name="nombre" required 
                               placeholder="Ej: Comer saludable">
                    </div>
                    
                    <div class="form-group">
                        <label for="metaDiaria">Meta Diaria *</label>
                        <input type="number" id="metaDiaria" name="metaDiaria" 
                               required min="1" value="1" placeholder="1">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" rows="3" 
                              placeholder="Describe el hábito..."></textarea>
                </div>
                
                <div class="form-group">
                    <label>Frecuencia *</label>
                    <div class="frecuencia-options">
                        <div class="frecuencia-option">
                            <input type="radio" id="diario" name="frecuencia" value="DIARIO" required>
                            <label for="diario">📅 Diario</label>
                        </div>
                        <div class="frecuencia-option">
                            <input type="radio" id="semanal" name="frecuencia" value="SEMANAL">
                            <label for="semanal">📆 Semanal</label>
                        </div>
                        <div class="frecuencia-option">
                            <input type="radio" id="mensual" name="frecuencia" value="MENSUAL">
                            <label for="mensual">📊 Mensual</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="fechaInicio">Fecha de Inicio *</label>
                    <input type="date" id="fechaInicio" name="fechaInicio" required>
                </div>
            </div>
            
            <div class="btn-group">
                <button type="button" class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/index.jsp'">
                    ← Volver al Inicio
                </button>
                <button type="submit" class="btn-primary" id="btn-guardar" disabled>
                    ✓ Crear Hábito y Asociar
                </button>
            </div>
        </form>
    </div>
    
    <script>
        let objetivoSeleccionado = null;
        
        // Auto-seleccionar fecha actual
        const fechaInicioInput = document.getElementById('fechaInicio');
        if (fechaInicioInput) {
            fechaInicioInput.valueAsDate = new Date();
        }
        
        // Event listeners para botones de editar
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                const id = this.getAttribute('data-id');
                console.log('✏️ Editando objetivo ID: ' + id);
                window.location.href = '${pageContext.request.contextPath}/controlador-objetivos?action=editar&id=' + id;
            });
        });
        
        // Event listeners para botones de eliminar
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                const id = this.getAttribute('data-id');
                const titulo = this.getAttribute('data-titulo');
                if (confirm('¿Estás seguro de que deseas eliminar el objetivo "' + titulo + '"?\n\nEsta acción no se puede deshacer.')) {
                    console.log('🗑️ Eliminando objetivo ID: ' + id);
                    window.location.href = '${pageContext.request.contextPath}/controlador-objetivos?action=eliminar&id=' + id;
                }
            });
        });
        
        // Agregar event listeners a todas las cards
        document.querySelectorAll('.objetivo-card').forEach(card => {
            card.addEventListener('click', function() {
                const objetivoId = this.getAttribute('data-objetivo-id');
                if (objetivoId) {
                    seleccionarObjetivo(Number(objetivoId));
                }
            });
        });
        
        // Auto-seleccionar el objetivo recién creado
        window.addEventListener('DOMContentLoaded', function() {
            const cardSeleccionada = document.querySelector('.objetivo-card.selected');
            if (cardSeleccionada) {
                const objetivoId = cardSeleccionada.getAttribute('data-objetivo-id');
                if (objetivoId) {
                    console.log('🎯 Auto-seleccionando objetivo recién creado: ' + objetivoId);
                    setTimeout(function() {
                        seleccionarObjetivo(Number(objetivoId));
                    }, 300);
                }
            }
        });
        
        // Función para seleccionar objetivo
        function seleccionarObjetivo(id) {
            console.log('📌 Seleccionando objetivo ID: ' + id);
            
            // Remover selección previa
            document.querySelectorAll('.objetivo-card').forEach(card => {
                card.classList.remove('selected');
            });
            
            // Seleccionar nuevo objetivo (buscar por data-objetivo-id)
            const cardSeleccionada = document.querySelector('.objetivo-card[data-objetivo-id="' + id + '"]');
            if (cardSeleccionada) {
                cardSeleccionada.classList.add('selected');
            }
            
            objetivoSeleccionado = id;
            document.getElementById('objetivoId').value = id;
            
            // Mostrar formulario y habilitar botón
            document.getElementById('formulario-habito').style.display = 'block';
            document.getElementById('btn-guardar').disabled = false;
            
            // Scroll suave al formulario
            setTimeout(function() {
                document.getElementById('formulario-habito').scrollIntoView({ 
                    behavior: 'smooth', 
                    block: 'start' 
                });
            }, 100);
        }
        
        // Mejorar UX de opciones de frecuencia
        document.querySelectorAll('.frecuencia-option').forEach(option => {
            option.addEventListener('click', function() {
                const radio = this.querySelector('input[type="radio"]');
                radio.checked = true;
            });
        });
        
        // Validación del formulario
        document.getElementById('form-habito').addEventListener('submit', function(e) {
            if (!objetivoSeleccionado) {
                e.preventDefault();
                alert('⚠️ Por favor selecciona un objetivo primero');
                return false;
            }
        });
    </script>
</body>
</html>
