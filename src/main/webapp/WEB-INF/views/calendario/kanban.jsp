<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Habito" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    @SuppressWarnings("unchecked")
    List<Habito> habitos = (List<Habito>) request.getAttribute("habitos");
    LocalDate fecha = (LocalDate) request.getAttribute("fecha");
    if (habitos == null) habitos = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Calendario Kanban - Sistema de Seguimiento</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
            background: #F7FAFC;
            min-height: 100vh;
            padding: 20px;
            color: #2D3748;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
        }
        
        .header {
            background: #FFFFFF;
            padding: 24px 32px;
            border-radius: 16px;
            margin-bottom: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: all 0.3s ease;
        }
        
        .header:hover {
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
        }
        
        .header h1 {
            color: #2D3748;
            font-size: 28px;
            font-weight: 700;
            letter-spacing: -0.5px;
        }
        
        .date-selector {
            display: flex;
            gap: 12px;
            align-items: center;
        }
        
        .date-selector input[type="date"] {
            padding: 12px 16px;
            border: 2px solid #E2E8F0;
            border-radius: 16px;
            font-size: 14px;
            color: #2D3748;
            background: #F7FAFC;
            transition: all 0.3s ease;
            font-weight: 500;
        }
        
        .date-selector input[type="date"]:focus {
            outline: none;
            border-color: #5A67D8;
            background: #FFFFFF;
            box-shadow: 0 0 0 3px rgba(90, 103, 216, 0.1);
        }
        
        .btn-today {
            background: #5A67D8;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 16px;
            cursor: pointer;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(90, 103, 216, 0.2);
        }
        
        .btn-today:hover {
            background: #667EEA;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(90, 103, 216, 0.3);
        }
        
        .btn-today:active {
            transform: translateY(0);
        }
        
        .time-slots {
            display: flex;
            gap: 24px;
            margin-bottom: 24px;
        }
        
        .time-slot {
            flex: 1;
            background: #FFFFFF;
            border-radius: 16px;
            padding: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        
        .time-slot:hover {
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
            transform: translateY(-2px);
        }
        
        .time-slot h3 {
            color: #2D3748;
            margin-bottom: 20px;
            font-size: 18px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 12px;
            letter-spacing: -0.3px;
        }
        
        .time-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        
        .morning .time-icon { background: linear-gradient(135deg, #FCD34D 0%, #FBBF24 100%); }
        .afternoon .time-icon { background: linear-gradient(135deg, #F59E0B 0%, #D97706 100%); }
        .evening .time-icon { background: linear-gradient(135deg, #A78BFA 0%, #8B5CF6 100%); }
        .night .time-icon { background: linear-gradient(135deg, #818CF8 0%, #6366F1 100%); }
        
        .kanban-board {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 16px;
            margin-top: 20px;
        }
        
        .kanban-column {
            background: #F7FAFC;
            border-radius: 16px;
            padding: 16px;
            min-height: 200px;
            border: 2px solid #E2E8F0;
            transition: all 0.3s ease;
        }
        
        .kanban-column:hover {
            border-color: #CBD5E0;
            background: #EDF2F7;
        }
        
        .kanban-column h4 {
            font-size: 13px;
            font-weight: 700;
            color: #718096;
            margin-bottom: 16px;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .por-hacer h4 { 
            border-left: 4px solid #CBD5E0; 
            padding-left: 12px;
            color: #718096;
        }
        .en-progreso h4 { 
            border-left: 4px solid #5A67D8; 
            padding-left: 12px;
            color: #5A67D8;
        }
        .completado h4 { 
            border-left: 4px solid #48BB78; 
            padding-left: 12px;
            color: #48BB78;
        }
        
        .task-card {
            background: #FFFFFF;
            border-radius: 16px;
            padding: 16px;
            margin-bottom: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            cursor: grab;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            border: 2px solid transparent;
        }
        
        .task-card:hover {
            box-shadow: 0 8px 20px rgba(90, 103, 216, 0.15);
            transform: translateY(-4px);
            border-color: #5A67D8;
        }
        
        .task-card:active {
            cursor: grabbing;
            transform: scale(0.95);
            opacity: 0.7;
        }
        
        .task-title {
            font-weight: 600;
            color: #2D3748;
            margin-bottom: 8px;
            font-size: 15px;
            line-height: 1.5;
            letter-spacing: -0.2px;
        }
        
        .task-time {
            font-size: 13px;
            color: #718096;
            display: flex;
            align-items: center;
            gap: 6px;
            margin-bottom: 10px;
            font-weight: 500;
        }
        
        .task-priority {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 16px;
            font-size: 11px;
            font-weight: 700;
            letter-spacing: 0.5px;
        }
        
        .priority-alta { 
            background: linear-gradient(135deg, #FEE2E2 0%, #FECACA 100%); 
            color: #E53E3E;
            box-shadow: 0 2px 4px rgba(229, 62, 62, 0.1);
        }
        .priority-media { 
            background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%); 
            color: #D97706;
            box-shadow: 0 2px 4px rgba(217, 119, 6, 0.1);
        }
        .priority-baja { 
            background: linear-gradient(135deg, #DBEAFE 0%, #BFDBFE 100%); 
            color: #5A67D8;
            box-shadow: 0 2px 4px rgba(90, 103, 216, 0.1);
        }
        
        .task-actions {
            margin-top: 12px;
            display: flex;
            gap: 8px;
        }
        
        .btn-action {
            flex: 1;
            padding: 8px 12px;
            border: none;
            border-radius: 16px;
            font-size: 12px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            letter-spacing: 0.3px;
        }
        
        .btn-iniciar { 
            background: linear-gradient(135deg, #5A67D8 0%, #667EEA 100%); 
            color: white;
            box-shadow: 0 2px 8px rgba(90, 103, 216, 0.3);
        }
        .btn-completar { 
            background: linear-gradient(135deg, #48BB78 0%, #38A169 100%); 
            color: white;
            box-shadow: 0 2px 8px rgba(72, 187, 120, 0.3);
        }
        .btn-revertir { 
            background: linear-gradient(135deg, #CBD5E0 0%, #A0AEC0 100%); 
            color: #2D3748;
            box-shadow: 0 2px 8px rgba(113, 128, 150, 0.2);
        }
        
        .btn-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        .btn-action:active {
            transform: translateY(0);
        }
        
        .empty-state {
            text-align: center;
            padding: 24px;
            color: #CBD5E0;
            font-size: 14px;
            font-weight: 500;
        }
        
        .drop-zone-hint {
            text-align: center;
            padding: 20px;
            color: #A0AEC0;
            font-size: 13px;
            font-style: italic;
            background: rgba(203, 213, 224, 0.1);
            border-radius: 12px;
            margin: 8px 0;
        }
        
        .drop-zone-active {
            background: rgba(90, 103, 216, 0.1) !important;
            border: 2px dashed #5A67D8 !important;
            color: #5A67D8;
        }
        
        .stats-bar {
            background: #FFFFFF;
            padding: 20px 28px;
            border-radius: 16px;
            margin-bottom: 24px;
            display: flex;
            gap: 40px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        
        .stats-bar:hover {
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
        }
        
        .stat-item {
            display: flex;
            align-items: center;
            gap: 16px;
        }
        
        .stat-icon {
            width: 56px;
            height: 56px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        .stat-icon.total { 
            background: linear-gradient(135deg, #DBEAFE 0%, #BFDBFE 100%); 
        }
        .stat-icon.progress { 
            background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%); 
        }
        .stat-icon.completed { 
            background: linear-gradient(135deg, #D1FAE5 0%, #A7F3D0 100%); 
        }
        
        .stat-info h4 {
            font-size: 13px;
            color: #718096;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 4px;
        }
        
        .stat-info p {
            font-size: 32px;
            font-weight: 800;
            color: #2D3748;
            letter-spacing: -1px;
        }
        
        .nav-buttons {
            display: flex;
            gap: 12px;
        }
        
        .btn-nav {
            background: #FFFFFF;
            color: #5A67D8;
            border: 2px solid #E2E8F0;
            padding: 12px 24px;
            border-radius: 16px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
            font-size: 14px;
        }
        
        .btn-nav:hover {
            background: #5A67D8;
            color: white;
            border-color: #5A67D8;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(90, 103, 216, 0.3);
        }
        
        .btn-nav:active {
            transform: translateY(0);
        }
        
        .habito-disponible {
            animation: pulse 2s infinite;
        }
        
        @keyframes pulse {
            0%, 100% { 
                box-shadow: 0 4px 12px rgba(90, 103, 216, 0.2); 
            }
            50% { 
                box-shadow: 0 8px 20px rgba(90, 103, 216, 0.4); 
            }
        }
        
        @keyframes bounce {
            0%, 100% { 
                transform: translateY(0); 
            }
            50% { 
                transform: translateY(-5px); 
            }
        }
        
        .dragging {
            opacity: 0.5;
            transform: rotate(5deg) scale(1.05);
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3) !important;
            z-index: 1000;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <h1>📅 Calendario Kanban</h1>
            <div class="date-selector">
                <input type="date" id="selectedDate" value="<%= LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) %>">
                <button class="btn-today" onclick="goToToday()">Hoy</button>
            </div>
            <div class="nav-buttons">
                <a href="habitos?action=dashboard" class="btn-nav">🏠 Dashboard</a>
                <a href="habitos?action=lista" class="btn-nav">📚 Mis Hábitos</a>
                <button onclick="sincronizarHabitos()" class="btn-nav" style="background: linear-gradient(135deg, #48BB78 0%, #38A169 100%); color: white; border: none;">🔄 Sincronizar</button>
                <a href="login.jsp" class="btn-nav" style="background: linear-gradient(135deg, #E53E3E 0%, #C53030 100%); color: white; border: none;">🔓 Cerrar Sesión</a>
            </div>
        </div>
        
        <!-- Sección de Hábitos Disponibles -->
        <div style="background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%); padding: 25px; border-radius: 16px; margin-bottom: 30px; box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);">
            <h2 style="color: white; margin: 0 0 20px 0; font-size: 22px; display: flex; align-items: center; gap: 10px;">
                <span style="font-size: 28px;">📚</span>
                Mis Hábitos Disponibles
                <span style="font-size: 14px; background: rgba(255,255,255,0.2); padding: 5px 12px; border-radius: 20px; margin-left: auto; animation: bounce 2s infinite;">
                    👇 Arrastra al Kanban
                </span>
            </h2>
            <div id="habitos-disponibles" style="display: flex; gap: 15px; flex-wrap: wrap; min-height: 120px; background: rgba(255,255,255,0.1); padding: 20px; border-radius: 12px; border: 2px dashed rgba(255,255,255,0.3);">
                <!-- Las cards de hábitos se cargan dinámicamente con JavaScript -->
            </div>
        </div>
        
        <!-- Stats Bar -->
        <div class="stats-bar">
            <div class="stat-item">
                <div class="stat-icon total">📋</div>
                <div class="stat-info">
                    <h4>Total Tareas</h4>
                    <p id="totalTasks">0</p>
                </div>
            </div>
            <div class="stat-item">
                <div class="stat-icon progress">⏳</div>
                <div class="stat-info">
                    <h4>En Progreso</h4>
                    <p id="progressTasks">0</p>
                </div>
            </div>
            <div class="stat-item">
                <div class="stat-icon completed">✅</div>
                <div class="stat-info">
                    <h4>Completadas</h4>
                    <p id="completedTasks">0</p>
                </div>
            </div>
        </div>
        
        <!-- Morning Slot -->
        <div class="time-slot morning">
            <h3>
                <span class="time-icon">🌅</span>
                Mañana (6:00 - 12:00)
            </h3>
            <div class="kanban-board">
                <div class="kanban-column por-hacer">
                    <h4>📝 Por Hacer</h4>
                    <div id="morning-todo" class="task-container"></div>
                </div>
                <div class="kanban-column en-progreso">
                    <h4>🔄 En Progreso</h4>
                    <div id="morning-progress" class="task-container"></div>
                </div>
                <div class="kanban-column completado">
                    <h4>✅ Completado</h4>
                    <div id="morning-done" class="task-container"></div>
                </div>
            </div>
        </div>
        
        <!-- Afternoon Slot -->
        <div class="time-slot afternoon">
            <h3>
                <span class="time-icon">☀️</span>
                Tarde (12:00 - 18:00)
            </h3>
            <div class="kanban-board">
                <div class="kanban-column por-hacer">
                    <h4>📝 Por Hacer</h4>
                    <div id="afternoon-todo" class="task-container"></div>
                </div>
                <div class="kanban-column en-progreso">
                    <h4>🔄 En Progreso</h4>
                    <div id="afternoon-progress" class="task-container"></div>
                </div>
                <div class="kanban-column completado">
                    <h4>✅ Completado</h4>
                    <div id="afternoon-done" class="task-container"></div>
                </div>
            </div>
        </div>
        
        <!-- Evening Slot -->
        <div class="time-slot evening">
            <h3>
                <span class="time-icon">🌆</span>
                Noche (18:00 - 22:00)
            </h3>
            <div class="kanban-board">
                <div class="kanban-column por-hacer">
                    <h4>📝 Por Hacer</h4>
                    <div id="evening-todo" class="task-container"></div>
                </div>
                <div class="kanban-column en-progreso">
                    <h4>🔄 En Progreso</h4>
                    <div id="evening-progress" class="task-container"></div>
                </div>
                <div class="kanban-column completado">
                    <h4>✅ Completado</h4>
                    <div id="evening-done" class="task-container"></div>
                </div>
            </div>
        </div>
        
        <!-- Night Slot -->
        <div class="time-slot night">
            <h3>
                <span class="time-icon">🌙</span>
                Madrugada (22:00 - 6:00)
            </h3>
            <div class="kanban-board">
                <div class="kanban-column por-hacer">
                    <h4>📝 Por Hacer</h4>
                    <div id="night-todo" class="task-container"></div>
                </div>
                <div class="kanban-column en-progreso">
                    <h4>🔄 En Progreso</h4>
                    <div id="night-progress" class="task-container"></div>
                </div>
                <div class="kanban-column completado">
                    <h4>✅ Completado</h4>
                    <div id="night-done" class="task-container"></div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Datos de hábitos del servidor
        const habitosDelServidor = [];
        <%
            if (habitos != null && !habitos.isEmpty()) {
                for (Habito h : habitos) {
                    Long habitoId = h.getId() != null ? h.getId() : 0L;
                    String nombreHabito = h.getNombre() != null ? h.getNombre().trim() : "Hábito sin nombre";
                    String descripcionHabito = h.getDescripcion() != null ? h.getDescripcion().trim() : "";
                    
                    String prioridad = "media";
                    if (h.getFrecuencia() != null) {
                        String frecuenciaStr = h.getFrecuencia().toString().toUpperCase();
                        if (frecuenciaStr.contains("DIAR")) prioridad = "alta";
                        else if (frecuenciaStr.contains("SEMAN")) prioridad = "media";
                        else prioridad = "baja";
                    }
                    
                    // Escapar caracteres especiales para JavaScript
                    nombreHabito = nombreHabito
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("'", "\\'")
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .replace("\t", " ");
                    
                    descripcionHabito = descripcionHabito
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("'", "\\'")
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .replace("\t", " ");
        %>
        habitosDelServidor.push({
            id: <%= habitoId %>,
            title: '<%= nombreHabito %>',
            time: '09:00',
            priority: '<%= prioridad %>',
            timeSlot: 'morning',
            status: 'todo',
            descripcion: '<%= descripcionHabito %>'
        });
        <%
                }
            }
        %>
        
        console.log('🎯 Hábitos cargados desde servidor:', habitosDelServidor.length);
        
        let tasks = [];
        const selectedDate = document.getElementById('selectedDate').value;
        const storageKey = 'kanbanTasks_' + selectedDate;
        const savedTasks = localStorage.getItem(storageKey);
        
        if (savedTasks) {
            try {
                tasks = JSON.parse(savedTasks);
                console.log('✅ Tareas cargadas desde localStorage:', tasks.length);
            } catch (e) {
                console.error('❌ Error al parsear localStorage:', e);
                tasks = [];
            }
        }
        
        function saveTasks() {
            const selectedDate = document.getElementById('selectedDate').value;
            const storageKey = 'kanbanTasks_' + selectedDate;
            localStorage.setItem(storageKey, JSON.stringify(tasks));
            console.log('💾 Guardado en localStorage:', tasks.length, 'tareas');
        }
        
        function sincronizarHabitos() {
            console.log('🔄 Sincronizando hábitos...');
            if (habitosDelServidor.length === 0) {
                alert('⚠️ No tienes hábitos registrados.\n\nVe a "Mis Hábitos" para crear tus primeros hábitos.');
                window.location.href = 'habitos?action=lista';
                return;
            }
            renderTasks();
            alert('✅ ¡Sincronizado!\n\n' + habitosDelServidor.length + ' hábitos disponibles\n' + tasks.length + ' hábitos en el Kanban');
        }
        
        function renderTasks() {
            // Limpiar todos los contenedores
            document.querySelectorAll('.task-container').forEach(container => {
                container.innerHTML = '';
            });
            
            // Renderizar hábitos disponibles
            renderHabitosDisponibles();
            
            // Renderizar tareas en el Kanban
            tasks.forEach(task => {
                const containerId = task.timeSlot + '-' + task.status;
                const container = document.getElementById(containerId);
                
                if (container) {
                    const taskCard = createTaskCard(task);
                    container.appendChild(taskCard);
                } else {
                    console.warn('Contenedor no encontrado: ' + containerId);
                }
            });
            
            updateStats();
        }
        
        function renderHabitosDisponibles() {
            const container = document.getElementById('habitos-disponibles');
            
            if (!container) {
                console.error('❌ Contenedor no encontrado');
                return;
            }
            
            container.innerHTML = '';
            
            if (habitosDelServidor.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; width: 100%; padding: 30px; color: white;">
                        <div style="font-size: 64px; margin-bottom: 20px;">📭</div>
                        <h3 style="margin: 0 0 10px 0; font-size: 22px; font-weight: 700;">No tienes hábitos registrados</h3>
                        <p style="margin: 0 0 20px 0; font-size: 16px; opacity: 0.9;">
                            Crea tus primeros hábitos para empezar
                        </p>
                        <a href="habitos?action=lista" style="background: white; color: #667EEA; padding: 14px 28px; border-radius: 12px; text-decoration: none; font-weight: 700; font-size: 16px; display: inline-block;">
                            ➕ Crear Mi Primer Hábito
                        </a>
                    </div>
                `;
                return;
            }
            
            habitosDelServidor.forEach((habito) => {
                const card = document.createElement('div');
                card.className = 'task-card habito-disponible';
                card.draggable = true;
                card.dataset.taskId = habito.id;
                card.style.cursor = 'grab';
                card.style.minWidth = '220px';
                card.style.maxWidth = '280px';
                
                const titulo = habito.title || 'Sin nombre';
                const tiempo = habito.time || '09:00';
                const prioridad = habito.priority || 'media';
                
                card.innerHTML = '<div class="task-title">' + titulo + '</div>' +
                    '<div class="task-time">🕐 ' + tiempo + '</div>' +
                    '<span class="task-priority priority-' + prioridad + '">' + prioridad.toUpperCase() + '</span>' +
                    '<div style="margin-top: 12px; padding-top: 12px; border-top: 2px dashed #E2E8F0; color: #5A67D8; font-size: 12px; font-weight: 700; text-align: center;">' +
                        '🖱️ ARRASTRA AL KANBAN' +
                    '</div>';
                
                card.addEventListener('dragstart', handleDragStart);
                card.addEventListener('dragend', handleDragEnd);
                
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'scale(1.05) translateY(-5px)';
                    this.style.boxShadow = '0 12px 24px rgba(90, 103, 216, 0.3)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'scale(1) translateY(0)';
                    this.style.boxShadow = '0 4px 12px rgba(0, 0, 0, 0.05)';
                });
                
                container.appendChild(card);
            });
        }
        
        function createTaskCard(task) {
            const card = document.createElement('div');
            card.className = 'task-card';
            card.draggable = true;
            card.dataset.taskId = task.id;
            
            card.addEventListener('dragstart', handleDragStart);
            card.addEventListener('dragend', handleDragEnd);
            
            let actions = '';
            if (task.status === 'todo') {
                actions = '<button class="btn-action btn-iniciar" onclick="moveTask(' + task.id + ', \'progress\')">Iniciar</button>';
            } else if (task.status === 'progress') {
                actions = '<button class="btn-action btn-revertir" onclick="moveTask(' + task.id + ', \'todo\')">Revertir</button>' +
                    '<button class="btn-action btn-completar" onclick="moveTask(' + task.id + ', \'done\')">Completar</button>';
            } else if (task.status === 'done') {
                actions = '<button class="btn-action btn-revertir" onclick="moveTask(' + task.id + ', \'progress\')">Reabrir</button>';
            }
            
            card.innerHTML = '<div class="task-title">' + task.title + '</div>' +
                '<div class="task-time">🕐 ' + task.time + '</div>' +
                '<span class="task-priority priority-' + task.priority + '">' + task.priority.toUpperCase() + '</span>' +
                '<div class="task-actions">' + actions + '</div>';
            
            return card;
        }
        
        let draggedTask = null;
        let draggedElement = null;
        
        function handleDragStart(e) {
            draggedElement = e.target;
            draggedTask = parseInt(e.target.dataset.taskId);
            
            e.target.classList.add('dragging');
            e.dataTransfer.effectAllowed = 'move';
            e.dataTransfer.setData('text/html', e.target.innerHTML);
            
            // Iluminar zonas de drop
            document.querySelectorAll('.task-container').forEach(container => {
                container.style.background = 'rgba(90, 103, 216, 0.08)';
                container.style.border = '2px dashed #5A67D8';
                if (!container.children.length) {
                    container.innerHTML = '<div class="drop-zone-hint">⬇️ Suelta aquí</div>';
                }
            });
        }
        
        function handleDragEnd(e) {
            e.target.classList.remove('dragging');
            
            // Limpiar zonas de drop
            document.querySelectorAll('.task-container').forEach(container => {
                container.style.background = '';
                container.style.border = '';
                container.classList.remove('drop-zone-active');
                
                // Eliminar hints vacíos
                const hint = container.querySelector('.drop-zone-hint');
                if (hint && !container.querySelector('.task-card')) {
                    hint.remove();
                }
            });
            
            draggedTask = null;
            draggedElement = null;
        }
        
        function handleDragOver(e) {
            if (e.preventDefault) {
                e.preventDefault();
            }
            e.currentTarget.classList.add('drop-zone-active');
            e.dataTransfer.dropEffect = 'move';
            return false;
        }
        
        function handleDragLeave(e) {
            e.currentTarget.classList.remove('drop-zone-active');
        }
        
        function handleDrop(e, newStatus, newTimeSlot) {
            e.preventDefault();
            e.stopPropagation();
            
            if (!draggedTask) return false;
            
            let task = tasks.find(t => t.id === draggedTask);
            
            // Si no existe en tasks, viene de hábitos disponibles
            if (!task) {
                const habitoOriginal = habitosDelServidor.find(h => h.id === draggedTask);
                
                if (habitoOriginal) {
                    // Verificar si ya existe en el Kanban
                    const yaExiste = tasks.find(t => t.id === draggedTask);
                    if (yaExiste) {
                        alert('⚠️ Este hábito ya está en el Kanban');
                        renderTasks();
                        return false;
                    }
                    
                    // Crear nueva tarea
                    task = {
                        ...habitoOriginal,
                        status: newStatus,
                        timeSlot: newTimeSlot
                    };
                    tasks.push(task);
                    console.log('✅ Hábito agregado al Kanban:', task.title);
                } else {
                    console.error('❌ Hábito no encontrado');
                    return false;
                }
            } else {
                // Actualizar posición existente
                task.status = newStatus;
                task.timeSlot = newTimeSlot;
                console.log('🔄 Hábito movido:', task.title);
            }
            
            saveTasks();
            renderTasks();
            
            return false;
        }
        
        function moveTask(taskId, newStatus) {
            const task = tasks.find(t => t.id === taskId);
            if (task) {
                task.status = newStatus;
                saveTasks();
                renderTasks();
            }
        }
        
        function setupDragAndDrop() {
            document.querySelectorAll('.task-container').forEach(container => {
                container.addEventListener('dragover', handleDragOver);
                container.addEventListener('dragleave', handleDragLeave);
                container.addEventListener('drop', function(e) {
                    const containerId = this.id;
                    const [timeSlot, status] = containerId.split('-');
                    handleDrop(e, status, timeSlot);
                });
            });
        }
        
        function updateStats() {
            const total = tasks.length;
            const progress = tasks.filter(t => t.status === 'progress').length;
            const completed = tasks.filter(t => t.status === 'done').length;
            
            document.getElementById('totalTasks').textContent = total;
            document.getElementById('progressTasks').textContent = progress;
            document.getElementById('completedTasks').textContent = completed;
        }
        
        function goToToday() {
            document.getElementById('selectedDate').value = new Date().toISOString().split('T')[0];
            // Recargar tareas para la nueva fecha
            const newStorageKey = 'kanbanTasks_' + document.getElementById('selectedDate').value;
            const savedTasks = localStorage.getItem(newStorageKey);
            if (savedTasks) {
                tasks = JSON.parse(savedTasks);
            } else {
                tasks = [];
            }
            renderTasks();
        }
        
        // Escuchar cambios de fecha
        document.getElementById('selectedDate').addEventListener('change', function() {
            const newStorageKey = 'kanbanTasks_' + this.value;
            const savedTasks = localStorage.getItem(newStorageKey);
            if (savedTasks) {
                tasks = JSON.parse(savedTasks);
            } else {
                tasks = [];
            }
            renderTasks();
        });
        
        // Inicialización
        document.addEventListener('DOMContentLoaded', function() {
            console.log('🚀 Inicializando Kanban...');
            console.log('📦 Hábitos disponibles:', habitosDelServidor.length);
            console.log('📋 Tareas en Kanban:', tasks.length);
            
            renderTasks();
            setupDragAndDrop();
            
            console.log('✅ Kanban inicializado correctamente');
        });
    </script>
</body>
</html>