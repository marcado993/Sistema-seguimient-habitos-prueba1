<%@ page import="com.example.dao.HabitoDAO" %>
<%@ page import="com.example.model.Habito" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>🔍 Diagnóstico Hibernate - Hábitos</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 30px;
            color: #2D3748;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
        }
        
        h1 {
            color: #667eea;
            margin-bottom: 10px;
            font-size: 36px;
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .subtitle {
            color: #718096;
            margin-bottom: 30px;
            font-size: 16px;
        }
        
        .status-box {
            background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
            color: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            font-size: 18px;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(72, 187, 120, 0.3);
        }
        
        .error-box {
            background: linear-gradient(135deg, #f56565 0%, #e53e3e 100%);
            color: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            font-size: 18px;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(245, 101, 101, 0.3);
        }
        
        .warning-box {
            background: linear-gradient(135deg, #ed8936 0%, #dd6b20 100%);
            color: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            font-size: 18px;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(237, 137, 54, 0.3);
        }
        
        .info-section {
            background: #f7fafc;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
        }
        
        .info-section h3 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 20px;
        }
        
        .habito-card {
            background: white;
            border: 2px solid #e2e8f0;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 15px;
            transition: all 0.3s ease;
        }
        
        .habito-card:hover {
            border-color: #667eea;
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
            transform: translateY(-2px);
        }
        
        .habito-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
        }
        
        .habito-title {
            font-size: 20px;
            font-weight: 700;
            color: #2D3748;
        }
        
        .badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            text-transform: uppercase;
        }
        
        .badge-success {
            background: #c6f6d5;
            color: #22543d;
        }
        
        .badge-danger {
            background: #fed7d7;
            color: #742a2a;
        }
        
        .habito-detail {
            color: #718096;
            margin-bottom: 8px;
            font-size: 14px;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            margin-right: 10px;
            margin-top: 20px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #e2e8f0;
            color: #2D3748;
        }
        
        .btn-secondary:hover {
            background: #cbd5e0;
        }
        
        .code-block {
            background: #2d3748;
            color: #68d391;
            padding: 15px;
            border-radius: 8px;
            font-family: 'Courier New', monospace;
            font-size: 13px;
            overflow-x: auto;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔍 Diagnóstico del Sistema</h1>
        <p class="subtitle">Verificación de Hibernate ORM + H2 Database Local</p>
        
        <%
            try {
                // Obtener usuario de sesión
                HttpSession sesion = request.getSession(false);
                String usuarioId = sesion != null ? (String) sesion.getAttribute("usuarioId") : null;
                
                // Si no hay usuario, usar "demo"
                if (usuarioId == null) {
                    usuarioId = "demo";
                    if (sesion != null) {
                        sesion.setAttribute("usuarioId", "demo");
                    }
                }
        %>
        
        <div class="info-section">
            <h3>📊 Información del Sistema</h3>
            <div class="habito-detail">
                <strong>🔧 Persistence Unit:</strong> sistema-seguimiento-pu
            </div>
            <div class="habito-detail">
                <strong>💾 Base de Datos:</strong> H2 (jdbc:h2:~/testdb)
            </div>
            <div class="habito-detail">
                <strong>🔄 Hibernate DDL:</strong> update (auto-generación de tablas)
            </div>
            <div class="habito-detail">
                <strong>👤 Usuario Actual:</strong> <%= usuarioId %>
            </div>
            <div class="habito-detail">
                <strong>🕐 Hora del Servidor:</strong> <%= new java.util.Date() %>
            </div>
        </div>
        
        <%
                // Intentar conectar y obtener hábitos
                HabitoDAO habitoDAO = new HabitoDAO();
                List<Habito> habitos = habitoDAO.findByUsuarioId(usuarioId);
                
                if (habitos != null && !habitos.isEmpty()) {
        %>
        
        <div class="status-box">
            ✅ Conexión exitosa a Hibernate + H2
        </div>
        
        <div class="info-section">
            <h3>📚 Hábitos Encontrados: <%= habitos.size() %></h3>
            
            <%
                for (Habito h : habitos) {
                    String nombre = h.getNombre() != null ? h.getNombre() : "Sin nombre";
                    String descripcion = h.getDescripcion() != null ? h.getDescripcion() : "Sin descripción";
                    String frecuencia = h.getFrecuencia() != null ? h.getFrecuencia().toString() : "N/A";
                    boolean activo = h.isActivo();
            %>
            
            <div class="habito-card">
                <div class="habito-header">
                    <div class="habito-title">
                        <%= nombre %>
                    </div>
                    <span class="badge <%= activo ? "badge-success" : "badge-danger" %>">
                        <%= activo ? "ACTIVO" : "INACTIVO" %>
                    </span>
                </div>
                <div class="habito-detail">
                    <strong>🆔 ID:</strong> <%= h.getId() %>
                </div>
                <div class="habito-detail">
                    <strong>📝 Descripción:</strong> <%= descripcion %>
                </div>
                <div class="habito-detail">
                    <strong>🔄 Frecuencia:</strong> <%= frecuencia %>
                </div>
                <div class="habito-detail">
                    <strong>🎯 Meta Diaria:</strong> <%= h.getMetaDiaria() != null ? h.getMetaDiaria() : "N/A" %>
                </div>
                <div class="habito-detail">
                    <strong>📅 Fecha Creación:</strong> <%= h.getFechaCreacion() != null ? h.getFechaCreacion() : "N/A" %>
                </div>
                <div class="habito-detail">
                    <strong>👤 Usuario ID:</strong> <%= h.getUsuarioId() %>
                </div>
            </div>
            
            <%
                }
            %>
        </div>
        
        <%
                } else {
        %>
        
        <div class="warning-box">
            ⚠️ No se encontraron hábitos para el usuario: <%= usuarioId %>
        </div>
        
        <div class="info-section">
            <h3>💡 Posibles Soluciones</h3>
            <div class="habito-detail">
                1. Ve a <strong>"Mis Hábitos"</strong> y crea algunos hábitos
            </div>
            <div class="habito-detail">
                2. Verifica que Hibernate esté creando las tablas correctamente
            </div>
            <div class="habito-detail">
                3. Revisa los logs del servidor para errores de Hibernate
            </div>
            <div class="habito-detail">
                4. Verifica que la base de datos H2 esté accesible en: ~/testdb
            </div>
        </div>
        
        <%
                }
                
            } catch (Exception e) {
        %>
        
        <div class="error-box">
            ❌ Error al conectar con Hibernate/H2
        </div>
        
        <div class="info-section">
            <h3>🐛 Detalles del Error</h3>
            <div class="habito-detail">
                <strong>Tipo:</strong> <%= e.getClass().getName() %>
            </div>
            <div class="habito-detail">
                <strong>Mensaje:</strong> <%= e.getMessage() %>
            </div>
            
            <div class="code-block">
<%
                e.printStackTrace(new java.io.PrintWriter(out));
%>
            </div>
        </div>
        
        <%
            }
        %>
        
        <a href="habitos?action=lista" class="btn btn-primary">📚 Ir a Mis Hábitos</a>
        <a href="calendario?action=kanban" class="btn btn-primary">📅 Ir al Kanban</a>
        <a href="habitos?action=dashboard" class="btn btn-secondary">🏠 Dashboard</a>
    </div>
</body>
</html>
