<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistema_seguimiento.dao.ObjetivoDAO" %>
<%@ page import="com.sistema_seguimiento.dao.HabitoDAO" %>
<%@ page import="com.sistema_seguimiento.model.Objetivo" %>
<%@ page import="com.sistema_seguimiento.model.Habito" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>🔍 Test de Persistencia - Objetivo + Hábito</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: #E9F7EF;
            padding: 20px;
            color: #555555;
        }
        
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }
        
        h1 {
            color: #555555;
            margin-bottom: 2rem;
            text-align: center;
        }
        
        .test-section {
            background: #F3E8FF;
            padding: 1.5rem;
            border-radius: 12px;
            margin-bottom: 1.5rem;
        }
        
        .success {
            background: #A8E6CF;
            color: #555555;
            padding: 1rem;
            border-radius: 8px;
            margin: 10px 0;
        }
        
        .error {
            background: #FFB6B9;
            color: #555555;
            padding: 1rem;
            border-radius: 8px;
            margin: 10px 0;
        }
        
        .info {
            background: #FFD6A5;
            color: #555555;
            padding: 1rem;
            border-radius: 8px;
            margin: 10px 0;
        }
        
        pre {
            background: #2D3748;
            color: #E2E8F0;
            padding: 1rem;
            border-radius: 8px;
            overflow-x: auto;
            margin: 10px 0;
        }
        
        .btn {
            padding: 12px 24px;
            background: #A8E6CF;
            color: #555555;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            margin: 5px;
        }
        
        .btn:hover {
            background: #90E0BC;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔍 Test de Persistencia Hibernate</h1>
        <p style="text-align: center; color: #888888; margin-bottom: 2rem;">
            Verificación de relación Objetivo → Hábito
        </p>
        
        <%
            String action = request.getParameter("action");
            ObjetivoDAO objetivoDAO = new ObjetivoDAO();
            HabitoDAO habitoDAO = new HabitoDAO();
            
            if ("crear".equals(action)) {
                // TEST 1: Crear Objetivo
                out.println("<div class='test-section'>");
                out.println("<h2>📋 TEST 1: Crear Objetivo</h2>");
                
                try {
                    Objetivo objetivo = new Objetivo();
                    objetivo.setTitulo("Test - Perder peso");
                    objetivo.setDescripcion("Objetivo de prueba para verificar persistencia");
                    objetivo.setUsuarioId("usuario_demo");
                    objetivo.setEstado(Objetivo.EstadoObjetivo.ACTIVO);
                    objetivo.setFechaLimite(LocalDateTime.now().plusDays(90));
                    
                    objetivo = objetivoDAO.save(objetivo);
                    
                    out.println("<div class='success'>");
                    out.println("✅ Objetivo creado exitosamente<br>");
                    out.println("ID: " + objetivo.getId() + "<br>");
                    out.println("Título: " + objetivo.getTitulo());
                    out.println("</div>");
                    
                    session.setAttribute("ultimoObjetivoId", objetivo.getId());
                    
                    // TEST 2: Crear Hábito asociado
                    out.println("<h2 style='margin-top: 2rem;'>🎯 TEST 2: Crear Hábito Asociado</h2>");
                    
                    Habito habito = new Habito();
                    habito.setNombre("Test - Comer saludable");
                    habito.setDescripcion("Hábito de prueba asociado al objetivo");
                    habito.setUsuarioId("usuario_demo");
                    habito.setFrecuencia(Habito.FrecuenciaHabito.DIARIO);
                    habito.setMetaDiaria(5);
                    habito.setFechaInicio(LocalDate.now());
                    habito.setObjetivo(objetivo);
                    
                    habito = habitoDAO.save(habito);
                    
                    out.println("<div class='success'>");
                    out.println("✅ Hábito creado y asociado exitosamente<br>");
                    out.println("ID Hábito: " + habito.getId() + "<br>");
                    out.println("Nombre: " + habito.getNombre() + "<br>");
                    out.println("Objetivo asociado: " + (habito.getObjetivo() != null ? habito.getObjetivo().getId() : "NULL"));
                    out.println("</div>");
                    
                    session.setAttribute("ultimoHabitoId", habito.getId());
                    
                } catch (Exception e) {
                    out.println("<div class='error'>");
                    out.println("❌ Error: " + e.getMessage());
                    out.println("<pre>");
                    e.printStackTrace(new java.io.PrintWriter(out));
                    out.println("</pre>");
                    out.println("</div>");
                }
                
                out.println("</div>");
            }
            
            if ("verificar".equals(action)) {
                out.println("<div class='test-section'>");
                out.println("<h2>🔍 TEST 3: Verificar Relaciones</h2>");
                
                Long objetivoId = (Long) session.getAttribute("ultimoObjetivoId");
                Long habitoId = (Long) session.getAttribute("ultimoHabitoId");
                
                if (objetivoId != null) {
                    try {
                        Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
                        
                        if (objetivo != null) {
                            out.println("<div class='info'>");
                            out.println("<h3>📋 Objetivo Recuperado:</h3>");
                            out.println("ID: " + objetivo.getId() + "<br>");
                            out.println("Título: " + objetivo.getTitulo() + "<br>");
                            out.println("Hábitos asociados: " + (objetivo.getHabitos() != null ? objetivo.getHabitos().size() : 0));
                            out.println("</div>");
                        } else {
                            out.println("<div class='error'>❌ Objetivo no encontrado</div>");
                        }
                    } catch (Exception e) {
                        out.println("<div class='error'>❌ Error al recuperar objetivo: " + e.getMessage() + "</div>");
                    }
                }
                
                if (habitoId != null) {
                    try {
                        Habito habito = habitoDAO.findById(habitoId).orElse(null);
                        
                        if (habito != null) {
                            out.println("<div class='info'>");
                            out.println("<h3>🎯 Hábito Recuperado:</h3>");
                            out.println("ID: " + habito.getId() + "<br>");
                            out.println("Nombre: " + habito.getNombre() + "<br>");
                            out.println("Objetivo ID: " + (habito.getObjetivo() != null ? habito.getObjetivo().getId() : "NULL") + "<br>");
                            out.println("Objetivo Título: " + (habito.getObjetivo() != null ? habito.getObjetivo().getTitulo() : "NULL"));
                            out.println("</div>");
                            
                            if (habito.getObjetivo() != null) {
                                out.println("<div class='success'>✅ ¡Relación verificada correctamente!</div>");
                            } else {
                                out.println("<div class='error'>❌ El hábito NO tiene objetivo asociado</div>");
                            }
                        } else {
                            out.println("<div class='error'>❌ Hábito no encontrado</div>");
                        }
                    } catch (Exception e) {
                        out.println("<div class='error'>❌ Error al recuperar hábito: " + e.getMessage() + "</div>");
                    }
                }
                
                out.println("</div>");
            }
            
            if ("listar".equals(action)) {
                out.println("<div class='test-section'>");
                out.println("<h2>📊 TEST 4: Listar Todos los Objetivos y Hábitos</h2>");
                
                try {
                    List<Objetivo> objetivos = objetivoDAO.findByUsuarioId("usuario_demo");
                    out.println("<h3>📋 Objetivos (Total: " + objetivos.size() + ")</h3>");
                    
                    for (Objetivo obj : objetivos) {
                        out.println("<div class='info'>");
                        out.println("<strong>" + obj.getTitulo() + "</strong> (ID: " + obj.getId() + ")<br>");
                        out.println("Hábitos asociados: " + (obj.getHabitos() != null ? obj.getHabitos().size() : 0));
                        
                        if (obj.getHabitos() != null && !obj.getHabitos().isEmpty()) {
                            out.println("<ul>");
                            for (Habito h : obj.getHabitos()) {
                                out.println("<li>" + h.getNombre() + " (ID: " + h.getId() + ")</li>");
                            }
                            out.println("</ul>");
                        }
                        out.println("</div>");
                    }
                    
                    List<Habito> habitos = habitoDAO.findByUsuarioId("usuario_demo");
                    out.println("<h3 style='margin-top: 1rem;'>🎯 Hábitos (Total: " + habitos.size() + ")</h3>");
                    
                    for (Habito hab : habitos) {
                        out.println("<div class='info'>");
                        out.println("<strong>" + hab.getNombre() + "</strong> (ID: " + hab.getId() + ")<br>");
                        out.println("Objetivo: " + (hab.getObjetivo() != null ? hab.getObjetivo().getTitulo() + " (ID: " + hab.getObjetivo().getId() + ")" : "Sin objetivo"));
                        out.println("</div>");
                    }
                    
                } catch (Exception e) {
                    out.println("<div class='error'>❌ Error: " + e.getMessage() + "</div>");
                    out.println("<pre>");
                    e.printStackTrace(new java.io.PrintWriter(out));
                    out.println("</pre>");
                }
                
                out.println("</div>");
            }
        %>
        
        <div style="text-align: center; margin-top: 2rem;">
            <a href="test-persistencia.jsp?action=crear" class="btn">🔨 Crear Test</a>
            <a href="test-persistencia.jsp?action=verificar" class="btn">🔍 Verificar</a>
            <a href="test-persistencia.jsp?action=listar" class="btn">📊 Listar Todo</a>
            <a href="index.jsp" class="btn" style="background: #FFB6B9;">🏠 Volver</a>
        </div>
        
        <div class="test-section" style="margin-top: 2rem;">
            <h3>📖 Instrucciones</h3>
            <ol>
                <li><strong>Crear Test:</strong> Crea un objetivo de prueba y un hábito asociado</li>
                <li><strong>Verificar:</strong> Verifica que las relaciones se guardaron correctamente</li>
                <li><strong>Listar Todo:</strong> Muestra todos los objetivos y hábitos con sus relaciones</li>
            </ol>
        </div>
        
        <div class="info" style="margin-top: 1rem;">
            <strong>💡 Nota:</strong> Revisa la consola del servidor para ver los logs de Hibernate SQL
        </div>
    </div>
</body>
</html>
