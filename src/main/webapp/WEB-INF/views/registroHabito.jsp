<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Habito" %>
<%@ page import="com.example.model.RegistroHabito" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro de Hábito - Sistema de Seguimiento</title>
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
        
        .registro-section {
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
        
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        
        .estado-options {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            margin-top: 10px;
        }
        
        .estado-card {
            padding: 20px;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 16px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            background: rgba(255, 255, 255, 0.4);
        }
        
        .estado-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }
        
        .estado-card input[type="radio"] {
            display: none;
        }
        
        .estado-card input[type="radio"]:checked + .estado-content {
            color: #555555;
        }
        
        .estado-card.cumplido input[type="radio"]:checked ~ * {
            color: #555555;
        }
        
        .estado-card.cumplido:has(input:checked) {
            background: #A8E6CF;
            border-color: #A8E6CF;
        }
        
        .estado-card.no-cumplido:has(input:checked) {
            background: #FFB6B9;
            border-color: #FFB6B9;
        }
        
        .estado-card.parcial:has(input:checked) {
            background: #FFD6A5;
            border-color: #FFD6A5;
        }
        
        .estado-content {
            font-size: 16px;
            font-weight: 600;
        }
        
        .estado-icon {
            font-size: 32px;
            margin-bottom: 10px;
            display: block;
        }
        
        .animo-grid {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 10px;
            margin-top: 10px;
        }
        
        .animo-emoji {
            width: 65px;
            height: 65px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 50%;
            cursor: pointer;
            transition: all 0.3s ease;
            background: rgba(255, 255, 255, 0.4);
        }
        
        .animo-emoji:hover {
            transform: scale(1.15);
        }
        
        .animo-emoji input[type="radio"] {
            display: none;
        }
        
        .animo-emoji:has(input:checked) {
            border-color: #FFD6A5;
            background: #FFD6A5;
            transform: scale(1.2);
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
        
        /* Estilos para cards de hábitos */
        .search-box {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 12px;
            font-size: 14px;
            font-family: 'Inter', sans-serif;
            background: rgba(255, 255, 255, 0.6);
            margin-bottom: 1.5rem;
            transition: all 0.3s ease;
        }
        
        .search-box:focus {
            outline: none;
            border-color: #FFD6A5;
            box-shadow: 0 0 0 3px rgba(255, 214, 165, 0.2);
            background: white;
        }
        
        .habitos-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 1rem;
            margin-bottom: 1.5rem;
            max-height: 400px;
            overflow-y: auto;
            padding: 10px;
        }
        
        .habito-card-select {
            background: white;
            border: 2px solid rgba(255, 255, 255, 0.6);
            border-radius: 16px;
            padding: 1.5rem;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .habito-card-select:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            border-color: #FFD6A5;
        }
        
        .habito-card-select.selected {
            background: #FFD6A5;
            border-color: #FFD6A5;
            box-shadow: 0 4px 12px rgba(255, 214, 165, 0.4);
        }
        
        .habito-card-select input[type="radio"] {
            position: absolute;
            opacity: 0;
            cursor: pointer;
        }
        
        .habito-card-title {
            font-family: 'Poppins', sans-serif;
            font-size: 16px;
            font-weight: 600;
            color: #555555;
            margin-bottom: 8px;
        }
        
        .habito-card-info {
            font-size: 13px;
            color: #888888;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .habito-icon {
            font-size: 32px;
            margin-bottom: 10px;
        }
        
        .no-habitos {
            text-align: center;
            padding: 3rem;
            color: #888888;
        }
        
        /* Botón Dashboard Header */
        .btn-dashboard {
            padding: 10px 20px;
            background: #FFB6B9;
            color: #555555;
            border: none;
            border-radius: 12px;
            font-size: 14px;
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
    <div class="container">
        <!-- Header con botón dashboard -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <h1 style="margin: 0;">📝 Registro de Hábito</h1>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn-dashboard">
                🏠 Dashboard
            </a>
        </div>
        
        <form action="controlador-habitos" method="post">
            <input type="hidden" name="action" value="registrar">
            <input type="hidden" id="habitoId" name="habitoId" required>
            
            <div class="registro-section">
                <h2 style="color: #555555; margin-bottom: 20px;">🎯 Selecciona tu Hábito</h2>
                
                <div class="form-group">
                    <input type="text" 
                           id="searchHabito" 
                           class="search-box" 
                           placeholder="🔍 Buscar hábito..." 
                           autocomplete="off">
                    
                    <div class="habitos-grid" id="habitosGrid">
                        <%
                            List<Habito> habitos = (List<Habito>) request.getAttribute("habitos");
                            if (habitos != null && !habitos.isEmpty()) {
                                for (Habito h : habitos) {
                        %>
                            <div class="habito-card-select" data-habito-nombre="<%= h.getNombre().toLowerCase() %>" onclick="selectHabito(this, '<%= h.getId() %>')">
                                <input type="radio" name="habitoRadio" value="<%= h.getId() %>" id="habito_<%= h.getId() %>">
                                <div class="habito-icon">🎯</div>
                                <div class="habito-card-title"><%= h.getNombre() %></div>
                                <div class="habito-card-info">
                                    <span>📊 Meta: <%= h.getMetaDiaria() != null ? h.getMetaDiaria() : 0 %></span>
                                    <% if (h.getFrecuencia() != null) { %>
                                        <span>| <%= h.getFrecuencia() %></span>
                                    <% } %>
                                </div>
                            </div>
                        <%
                                }
                            } else {
                        %>
                            <div class="no-habitos">
                                <div style="font-size: 48px; margin-bottom: 1rem;">📭</div>
                                <p>No hay hábitos disponibles</p>
                                <p style="font-size: 13px; margin-top: 8px;">Crea tu primer hábito para comenzar</p>
                            </div>
                        <%
                            }
                        %>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="fecha">Fecha del Registro *</label>
                    <input type="date" id="fecha" name="fecha" required>
                </div>
                
                <div class="form-group">
                    <label>Estado del Cumplimiento *</label>
                    <div class="estado-options">
                        <div class="estado-card cumplido">
                            <input type="radio" id="cumplido" name="estado" value="CUMPLIDO" required>
                            <label for="cumplido">
                                <span class="estado-icon">✅</span>
                                <div class="estado-content">Cumplido</div>
                            </label>
                        </div>
                        <div class="estado-card no-cumplido">
                            <input type="radio" id="noCumplido" name="estado" value="NO_CUMPLIDO">
                            <label for="noCumplido">
                                <span class="estado-icon">❌</span>
                                <div class="estado-content">No Cumplido</div>
                            </label>
                        </div>
                        <div class="estado-card parcial">
                            <input type="radio" id="parcial" name="estado" value="PARCIAL">
                            <label for="parcial">
                                <span class="estado-icon">⏳</span>
                                <div class="estado-content">Parcial</div>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="registro-section">
                <h2 style="color: #555555; margin-bottom: 20px;">😊 Estado de Ánimo</h2>
                
                <div class="form-group">
                    <label>¿Cómo te sientes?</label>
                    <div class="animo-grid">
                        <div class="animo-emoji">
                            <input type="radio" id="muy-mal" name="estadoAnimo" value="MUY_MAL">
                            <label for="muy-mal">😢</label>
                        </div>
                        <div class="animo-emoji">
                            <input type="radio" id="mal" name="estadoAnimo" value="MAL">
                            <label for="mal">😞</label>
                        </div>
                        <div class="animo-emoji">
                            <input type="radio" id="neutral" name="estadoAnimo" value="NEUTRAL" checked>
                            <label for="neutral">😐</label>
                        </div>
                        <div class="animo-emoji">
                            <input type="radio" id="bien" name="estadoAnimo" value="BIEN">
                            <label for="bien">😊</label>
                        </div>
                        <div class="animo-emoji">
                            <input type="radio" id="muy-bien" name="estadoAnimo" value="MUY_BIEN">
                            <label for="muy-bien">😄</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="observacion">Observaciones</label>
                    <textarea id="observacion" name="observacion" 
                              placeholder="¿Cómo te fue hoy? ¿Qué aprendiste?"></textarea>
                </div>
            </div>
            
            <div class="btn-group">
                <button type="button" class="btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/index.jsp'">
                    🏠 Volver al Dashboard
                </button>
                <button type="submit" class="btn-primary">
                    ✓ Guardar Registro
                </button>
            </div>
        </form>
    </div>
    
    <script>
        // Auto-seleccionar fecha actual
        document.getElementById('fecha').valueAsDate = new Date();
        
        // Función para seleccionar hábito
        function selectHabito(card, habitoId) {
            // Remover selección previa
            document.querySelectorAll('.habito-card-select').forEach(c => {
                c.classList.remove('selected');
            });
            
            // Seleccionar card actual
            card.classList.add('selected');
            
            // Actualizar input hidden con el ID del hábito
            document.getElementById('habitoId').value = habitoId;
            
            // Marcar radio button
            const radio = card.querySelector('input[type="radio"]');
            if (radio) {
                radio.checked = true;
            }
        }
        
        // Búsqueda de hábitos
        document.getElementById('searchHabito').addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const cards = document.querySelectorAll('.habito-card-select');
            
            cards.forEach(card => {
                const habitoNombre = card.getAttribute('data-habito-nombre');
                if (habitoNombre.includes(searchTerm)) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
            
            // Mostrar mensaje si no hay resultados
            const visibleCards = document.querySelectorAll('.habito-card-select[style="display: block;"], .habito-card-select:not([style*="display: none"])');
            const noResults = document.getElementById('noResults');
            
            if (visibleCards.length === 0 && searchTerm !== '') {
                if (!noResults) {
                    const grid = document.getElementById('habitosGrid');
                    const msg = document.createElement('div');
                    msg.id = 'noResults';
                    msg.className = 'no-habitos';
                    msg.innerHTML = `
                        <div style="font-size: 48px; margin-bottom: 1rem;">🔍</div>
                        <p>No se encontraron hábitos con "<strong>${searchTerm}</strong>"</p>
                    `;
                    grid.appendChild(msg);
                }
            } else if (noResults) {
                noResults.remove();
            }
        });
        
        // Validación del formulario
        document.querySelector('form').addEventListener('submit', function(e) {
            const habitoId = document.getElementById('habitoId').value;
            if (!habitoId) {
                e.preventDefault();
                alert('⚠️ Por favor, selecciona un hábito antes de continuar');
                document.getElementById('searchHabito').focus();
                return false;
            }
        });
    </script>
</body>
</html>
