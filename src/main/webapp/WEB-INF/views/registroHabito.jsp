<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistema_seguimiento.model.Habito" %>
<%@ page import="com.sistema_seguimiento.model.RegistroHabito" %>
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
        
        /* 🎨 EFECTO DE CARGA - FADE IN */
        body {
            font-family: 'Inter', 'Segoe UI', sans-serif;
            background: #E9F7EF;
            min-height: 100vh;
            padding: 20px;
            color: #555555;
            animation: fadeIn 0.6s ease-in;
        }
        
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        /* 💫 LOADING SPINNER */
        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(233, 247, 239, 0.95);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.3s ease;
        }
        
        .loading-overlay.active {
            opacity: 1;
            pointer-events: all;
        }
        
        .spinner {
            width: 50px;
            height: 50px;
            border: 4px solid #D5F5E3;
            border-top: 4px solid #27AE60;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .loading-text {
            position: absolute;
            margin-top: 90px;
            color: #27AE60;
            font-weight: 600;
            font-size: 14px;
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
        
        /* ✅ NUEVO: Container vertical para cards + formulario */
        .habitos-container {
            display: flex;
            flex-direction: column;
            gap: 1rem;
            margin-bottom: 1.5rem;
            max-height: 600px;
            overflow-y: auto;
            padding: 10px;
        }
        
        .habito-wrapper {
            display: flex;
            flex-direction: column;
            gap: 1rem;
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
        
        /* ✅ NUEVO: Formulario dinámico debajo de card */
        .formulario-registro-dinamico {
            background: #f9f9f9;
            border: 2px solid #A8E6CF;
            border-radius: 12px;
            padding: 1.5rem;
            animation: slideDown 0.3s ease-out;
        }
        
        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        /* ✅ NUEVO: Estilos para selector de estado de ánimo */
        .animo-grid {
            display: flex;
            gap: 12px;
            justify-content: center;
            margin-top: 10px;
        }
        
        .animo-emoji {
            position: relative;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .animo-emoji input[type="radio"] {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }
        
        .animo-emoji span {
            display: inline-block;
            font-size: 36px;
            padding: 10px;
            border: 3px solid transparent;
            border-radius: 50%;
            background: #f5f5f5;
            transition: all 0.3s ease;
        }
        
        .animo-emoji:hover span {
            transform: scale(1.15);
            background: #e8f5e9;
        }
        
        .animo-emoji input[type="radio"]:checked + span {
            border-color: #A8E6CF;
            background: #A8E6CF;
            transform: scale(1.2);
            box-shadow: 0 4px 12px rgba(168, 230, 207, 0.5);
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
        
        /* Estilos del Pomodoro Timer */
        .pomodoro-section {
            background: white;
            padding: 2rem;
            border-radius: 16px;
            margin-bottom: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            text-align: center;
        }
        
        .pomodoro-header {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-bottom: 1.5rem;
        }
        
        .pomodoro-title {
            font-family: 'Poppins', sans-serif;
            color: #555555;
            font-weight: 600;
            font-size: 24px;
        }
        
        .timer-display {
            font-family: 'Poppins', sans-serif;
            font-size: 72px;
            font-weight: 700;
            color: #555555;
            margin: 2rem 0;
            letter-spacing: 3px;
        }
        
        .timer-state {
            display: inline-block;
            padding: 12px 24px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 16px;
            margin-bottom: 1.5rem;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .state-trabajo {
            background: #FFD6A5;
            color: #555555;
        }
        
        .state-descanso {
            background: #A8E6CF;
            color: #555555;
        }
        
        .state-descanso-largo {
            background: #FFB6B9;
            color: #555555;
        }
        
        .timer-controls {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-bottom: 2rem;
        }
        
        .btn-timer {
            padding: 15px 30px;
            border: none;
            border-radius: 16px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            min-width: 140px;
        }
        
        .btn-start {
            background: #A8E6CF;
            color: #555555;
        }
        
        .btn-start:hover {
            background: #90E0BC;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(168, 230, 207, 0.4);
        }
        
        .btn-pause {
            background: #FFD6A5;
            color: #555555;
        }
        
        .btn-pause:hover {
            background: #FFC78C;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 214, 165, 0.4);
        }
        
        .btn-reset {
            background: #FFB6B9;
            color: #555555;
        }
        
        .btn-reset:hover {
            background: #FFA5A8;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 182, 185, 0.4);
        }
        
        .btn-timer:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            transform: none;
        }
        
        .pomodoro-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 1rem;
            margin-top: 2rem;
            padding-top: 2rem;
            border-top: 2px solid #f0f0f0;
        }
        
        .pomodoro-stat {
            text-align: center;
        }
        
        .pomodoro-stat-value {
            font-family: 'Poppins', sans-serif;
            font-size: 28px;
            font-weight: 700;
            color: #FFD6A5;
        }
        
        .pomodoro-stat-label {
            font-size: 12px;
            color: #888888;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-top: 5px;
        }
        
        .timer-progress {
            width: 100%;
            max-width: 500px;
            height: 8px;
            background: rgba(0, 0, 0, 0.05);
            border-radius: 4px;
            margin: 0 auto 2rem;
            overflow: hidden;
        }
        
        .timer-progress-bar {
            height: 100%;
            background: #A8E6CF;
            transition: width 0.3s ease;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <!-- 💫 LOADING OVERLAY -->
    <div class="loading-overlay" id="loadingOverlay">
        <div>
            <div class="spinner"></div>
            <div class="loading-text">Cargando...</div>
        </div>
    </div>
    
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
                    
                    <!-- ✅ NUEVO: Container para cards + formulario intercalado -->
                    <div class="habitos-container" id="habitosContainer">
                        <%
                            List<Habito> habitos = (List<Habito>) request.getAttribute("habitos");
                            if (habitos != null && !habitos.isEmpty()) {
                                for (Habito h : habitos) {
                        %>
                            <div class="habito-wrapper" data-habito-id="<%= h.getId() %>">
                                <div class="habito-card-select" 
                                     data-habito-nombre="<%= h.getNombre().toLowerCase() %>" 
                                     onclick="selectHabito(this, '<%= h.getId() %>')"
                                     data-meta="<%= h.getMetaDiaria() != null ? h.getMetaDiaria() : 1 %>">
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
                                <!-- El formulario se insertará aquí dinámicamente -->
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
                
                <!-- ✅ NUEVO: Formulario de registro (oculto inicialmente, se clonará) -->
                <div id="formularioRegistroTemplate" style="display: none;">
                    <div class="formulario-registro-dinamico">
                        <div class="form-group">
                            <label for="fecha">Fecha del Registro *</label>
                            <input type="date" id="fecha" name="fecha" required>
                        </div>
                        
                        <div class="form-group">
                            <label>¿Completaste el hábito? *</label>
                            <div class="estado-options">
                                <div class="estado-card cumplido">
                                    <input type="radio" name="estado" value="CUMPLIDO" required>
                                    <label>
                                        <span class="estado-icon">✅</span>
                                        <div class="estado-content">Cumplido</div>
                                    </label>
                                </div>
                                <div class="estado-card no-cumplido">
                                    <input type="radio" name="estado" value="NO_CUMPLIDO">
                                    <label>
                                        <span class="estado-icon">❌</span>
                                        <div class="estado-content">No Cumplido</div>
                                    </label>
                                </div>
                                <div class="estado-card parcial">
                                    <input type="radio" name="estado" value="PARCIAL">
                                    <label>
                                        <span class="estado-icon">⏳</span>
                                        <div class="estado-content">Parcial</div>
                                    </label>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="vecesRealizado">¿Cuántas veces lo realizaste? *</label>
                            <input type="number" 
                                   id="vecesRealizado" 
                                   name="vecesRealizado" 
                                   min="0" 
                                   value="1" 
                                   required
                                   style="padding: 12px; border: 2px solid #e0e0e0; border-radius: 8px; width: 100%; font-size: 14px;">
                        </div>
                        
                        <!-- ✅ NUEVO: Estado de Ánimo al registrar cumplimiento -->
                        <div class="form-group">
                            <label>¿Cómo te sientes hoy? 😊</label>
                            <div class="animo-grid">
                                <label class="animo-emoji">
                                    <input type="radio" name="estadoAnimo" value="muy_feliz">
                                    <span>😁</span>
                                </label>
                                <label class="animo-emoji">
                                    <input type="radio" name="estadoAnimo" value="feliz">
                                    <span>😊</span>
                                </label>
                                <label class="animo-emoji">
                                    <input type="radio" name="estadoAnimo" value="neutral" checked>
                                    <span>😐</span>
                                </label>
                                <label class="animo-emoji">
                                    <input type="radio" name="estadoAnimo" value="triste">
                                    <span>😔</span>
                                </label>
                                <label class="animo-emoji">
                                    <input type="radio" name="estadoAnimo" value="muy_triste">
                                    <span>😢</span>
                                </label>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="notas">Notas</label>
                            <textarea id="notas" name="notas" 
                                      placeholder="¿Cómo te fue? ¿Qué aprendiste? (Opcional)"
                                      style="padding: 12px; border: 2px solid #e0e0e0; border-radius: 8px; width: 100%; min-height: 80px; font-size: 14px; font-family: inherit;"></textarea>
                        </div>
                        
                        <div class="btn-group">
                            <button type="button" class="btn-secondary" onclick="cancelarRegistro()">
                                ✖️ Cancelar
                            </button>
                            <button type="submit" class="btn-primary">
                                ✓ Guardar Registro
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Pomodoro Timer Section (Opcional - se muestra aparte) -->
            <div class="pomodoro-section" style="margin-top: 2rem;">
                <div class="pomodoro-header">
                    <span style="font-size: 32px;">🍅</span>
                    <h2 class="pomodoro-title">Temporizador Pomodoro (Opcional)</h2>
                </div>
                
                <div class="timer-state state-trabajo" id="timerState">
                    ⚡ TRABAJO
                </div>
                
                <div class="timer-progress">
                    <div class="timer-progress-bar" id="timerProgressBar" style="width: 100%;"></div>
                </div>
                
                <div class="timer-display" id="timerDisplay">
                    25:00
                </div>
                
                <div class="timer-controls">
                    <button type="button" class="btn-timer btn-start" id="btnStart" onclick="startTimer()">
                        ▶️ Iniciar
                    </button>
                    <button type="button" class="btn-timer btn-pause" id="btnPause" onclick="pauseTimer()" disabled>
                        ⏸️ Pausar
                    </button>
                    <button type="button" class="btn-timer btn-reset" id="btnReset" onclick="resetTimer()">
                        🔄 Reiniciar
                    </button>
                </div>
                
                <div class="pomodoro-stats">
                    <div class="pomodoro-stat">
                        <div class="pomodoro-stat-value" id="pomodorosCompletados">0</div>
                        <div class="pomodoro-stat-label">🍅 Pomodoros</div>
                    </div>
                    <div class="pomodoro-stat">
                        <div class="pomodoro-stat-value" id="tiempoTotal">0h 0m</div>
                        <div class="pomodoro-stat-label">⏱️ Tiempo Total</div>
                    </div>
                    <div class="pomodoro-stat">
                        <div class="pomodoro-stat-value" id="cicloActual">1/4</div>
                        <div class="pomodoro-stat-label">🔄 Ciclo</div>
                    </div>
                </div>
            </div>
            
            <div class="btn-group" style="margin-top: 2rem;">
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
        
        // ✅ NUEVO: Función para seleccionar hábito e insertar formulario debajo
        function selectHabito(card, habitoId) {
            // Remover selección previa y formularios previos
            document.querySelectorAll('.habito-card-select').forEach(c => {
                c.classList.remove('selected');
            });
            
            // Eliminar formularios existentes
            document.querySelectorAll('.formulario-registro-dinamico').forEach(f => {
                f.remove();
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
            
            // ✅ Clonar e insertar formulario debajo de esta card
            const template = document.getElementById('formularioRegistroTemplate');
            const formulario = template.cloneNode(true);
            formulario.id = 'formularioActivo';
            formulario.style.display = 'block';
            
            // Obtener la meta diaria del hábito
            const metaDiaria = card.getAttribute('data-meta');
            const inputVeces = formulario.querySelector('[name="vecesRealizado"]');
            if (inputVeces && metaDiaria) {
                inputVeces.value = metaDiaria;
                inputVeces.setAttribute('max', metaDiaria * 3); // Máximo 3x la meta
            }
            
            // Insertar después de la card
            const wrapper = card.closest('.habito-wrapper');
            wrapper.appendChild(formulario.firstElementChild);
            
            // Scroll suave al formulario
            setTimeout(() => {
                formulario.firstElementChild.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }, 100);
            
            console.log('📝 Hábito seleccionado:', habitoId, '| Meta diaria:', metaDiaria);
        }
        
        // ✅ NUEVO: Función para cancelar registro
        function cancelarRegistro() {
            // Eliminar formulario y quitar selección
            document.querySelectorAll('.formulario-registro-dinamico').forEach(f => {
                f.remove();
            });
            document.querySelectorAll('.habito-card-select').forEach(c => {
                c.classList.remove('selected');
            });
            document.getElementById('habitoId').value = '';
            console.log('❌ Registro cancelado');
        }
        
        // Búsqueda de hábitos
        document.getElementById('searchHabito').addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const wrappers = document.querySelectorAll('.habito-wrapper');
            
            wrappers.forEach(wrapper => {
                const card = wrapper.querySelector('.habito-card-select');
                const habitoNombre = card.getAttribute('data-habito-nombre');
                if (habitoNombre.includes(searchTerm)) {
                    wrapper.style.display = 'flex';
                } else {
                    wrapper.style.display = 'none';
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
        
        // ==================== POMODORO TIMER ====================
        
        // Estados del Pomodoro
        const Estados = {
            TRABAJO: { nombre: 'TRABAJO', tiempo: 25 * 60, emoji: '⚡', clase: 'state-trabajo', color: '#A8E6CF' },
            DESCANSO_CORTO: { nombre: 'DESCANSO CORTO', tiempo: 5 * 60, emoji: '☕', clase: 'state-descanso', color: '#A8E6CF' },
            DESCANSO_LARGO: { nombre: 'DESCANSO LARGO', tiempo: 15 * 60, emoji: '🎉', clase: 'state-descanso-largo', color: '#FFB6B9' }
        };
        
        // Variables globales del timer
        let tiempoRestante = Estados.TRABAJO.tiempo;
        let tiempoTotal = Estados.TRABAJO.tiempo;
        let estadoActual = Estados.TRABAJO;
        let timerInterval = null;
        let isRunning = false;
        let pomodorosCompletados = 0;
        let tiempoTotalTrabajado = 0; // en segundos
        let cicloActual = 1;
        
        // Elementos del DOM
        const timerDisplay = document.getElementById('timerDisplay');
        const timerState = document.getElementById('timerState');
        const timerProgressBar = document.getElementById('timerProgressBar');
        const btnStart = document.getElementById('btnStart');
        const btnPause = document.getElementById('btnPause');
        const btnReset = document.getElementById('btnReset');
        const pomodorosDisplay = document.getElementById('pomodorosCompletados');
        const tiempoTotalDisplay = document.getElementById('tiempoTotal');
        const cicloActualDisplay = document.getElementById('cicloActual');
        
        // Función para formatear tiempo MM:SS
        function formatTime(seconds) {
            const mins = Math.floor(seconds / 60);
            const secs = seconds % 60;
            return mins.toString().padStart(2, '0') + ':' + secs.toString().padStart(2, '0');
        }
        
        // Función para actualizar la visualización
        function updateDisplay() {
            timerDisplay.textContent = formatTime(tiempoRestante);
            
            // Actualizar barra de progreso
            const progreso = ((tiempoTotal - tiempoRestante) / tiempoTotal) * 100;
            timerProgressBar.style.width = progreso + '%';
            timerProgressBar.style.background = estadoActual.color;
            
            // Actualizar estado
            timerState.textContent = estadoActual.emoji + ' ' + estadoActual.nombre;
            timerState.className = 'timer-state ' + estadoActual.clase;
            
            // Actualizar estadísticas
            pomodorosDisplay.textContent = pomodorosCompletados;
            const horas = Math.floor(tiempoTotalTrabajado / 3600);
            const minutos = Math.floor((tiempoTotalTrabajado % 3600) / 60);
            tiempoTotalDisplay.textContent = horas + 'h ' + minutos + 'm';
            cicloActualDisplay.textContent = cicloActual + '/4';
        }
        
        // Función para cambiar de estado
        function cambiarEstado() {
            if (estadoActual === Estados.TRABAJO) {
                pomodorosCompletados++;
                tiempoTotalTrabajado += Estados.TRABAJO.tiempo;
                
                // Después de 4 pomodoros, descanso largo
                if (pomodorosCompletados % 4 === 0) {
                    estadoActual = Estados.DESCANSO_LARGO;
                    cicloActual = 1; // Reiniciar ciclo
                } else {
                    estadoActual = Estados.DESCANSO_CORTO;
                    cicloActual++;
                }
            } else {
                // Volver a trabajo después de cualquier descanso
                estadoActual = Estados.TRABAJO;
            }
            
            tiempoRestante = estadoActual.tiempo;
            tiempoTotal = estadoActual.tiempo;
            updateDisplay();
            
            // Mostrar notificación
            if (Notification.permission === "granted") {
                new Notification("Pomodoro - " + estadoActual.nombre, {
                    body: "Es hora de " + (estadoActual === Estados.TRABAJO ? "trabajar" : "descansar"),
                    icon: "🍅"
                });
            }
        }
        
        // Función principal del timer
        function tick() {
            if (tiempoRestante > 0) {
                tiempoRestante--;
                updateDisplay();
            } else {
                // Timer completado
                pauseTimer();
                cambiarEstado();
            }
        }
        
        // Iniciar timer
        function startTimer() {
            if (!isRunning) {
                isRunning = true;
                timerInterval = setInterval(tick, 1000);
                
                btnStart.disabled = true;
                btnPause.disabled = false;
                
                // Solicitar permiso para notificaciones
                if (Notification.permission === "default") {
                    Notification.requestPermission();
                }
            }
        }
        
        // Pausar timer
        function pauseTimer() {
            if (isRunning) {
                isRunning = false;
                clearInterval(timerInterval);
                timerInterval = null;
                
                btnStart.disabled = false;
                btnPause.disabled = true;
                
                btnStart.innerHTML = '▶️ Continuar';
            }
        }
        
        // Reiniciar timer
        function resetTimer() {
            pauseTimer();
            
            // Volver al estado de trabajo
            estadoActual = Estados.TRABAJO;
            tiempoRestante = Estados.TRABAJO.tiempo;
            tiempoTotal = Estados.TRABAJO.tiempo;
            
            updateDisplay();
            
            btnStart.innerHTML = '▶️ Iniciar';
            btnStart.disabled = false;
            btnPause.disabled = true;
        }
        
        // Inicializar display
        updateDisplay();
        
        // Guardar estado en localStorage
        window.addEventListener('beforeunload', function() {
            localStorage.setItem('pomodoroState', JSON.stringify({
                pomodorosCompletados: pomodorosCompletados,
                tiempoTotalTrabajado: tiempoTotalTrabajado,
                cicloActual: cicloActual
            }));
        });
        
        // Recuperar estado desde localStorage
        window.addEventListener('load', function() {
            const savedState = localStorage.getItem('pomodoroState');
            if (savedState) {
                const state = JSON.parse(savedState);
                pomodorosCompletados = state.pomodorosCompletados || 0;
                tiempoTotalTrabajado = state.tiempoTotalTrabajado || 0;
                cicloActual = state.cicloActual || 1;
                updateDisplay();
            }
        });
        
        // 🎨 FUNCIONES DE LOADING
        function showLoading() {
            document.getElementById('loadingOverlay').classList.add('active');
        }
        
        // Mostrar loading al enviar formulario
        document.querySelector('form').addEventListener('submit', function() {
            showLoading();
        });
        
        // Mostrar loading en enlaces de navegación
        document.querySelectorAll('a').forEach(link => {
            link.addEventListener('click', function(e) {
                setTimeout(() => showLoading(), 100);
            });
        });
    </script>
</body>
</html>
