<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.dao.HabitoDAO" %>
<%@ page import="com.example.model.Habito" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test - Hábitos en DB</title>
</head>
<body>
    <h1>Verificación de Hábitos en Base de Datos</h1>
    <%
        try {
            HabitoDAO habitoDAO = new HabitoDAO();
            
            // Obtener usuario de la sesión
            String usuarioId = (String) session.getAttribute("usuarioId");
            
            out.println("<h2>Usuario ID: " + usuarioId + "</h2>");
            
            if (usuarioId != null) {
                List<Habito> habitos = habitoDAO.findByUsuarioId(usuarioId);
                
                out.println("<h3>Total de hábitos encontrados: " + habitos.size() + "</h3>");
                
                if (habitos.isEmpty()) {
                    out.println("<p style='color: red;'>⚠️ NO HAY HÁBITOS REGISTRADOS PARA ESTE USUARIO</p>");
                    out.println("<p><a href='habitos?action=lista'>Ir a crear hábitos</a></p>");
                } else {
                    out.println("<table border='1' cellpadding='10'>");
                    out.println("<tr><th>ID</th><th>Nombre</th><th>Descripción</th><th>Frecuencia</th><th>Activo</th></tr>");
                    
                    for (Habito h : habitos) {
                        out.println("<tr>");
                        out.println("<td>" + h.getId() + "</td>");
                        out.println("<td><strong>" + h.getNombre() + "</strong></td>");
                        out.println("<td>" + (h.getDescripcion() != null ? h.getDescripcion() : "-") + "</td>");
                        out.println("<td>" + h.getFrecuencia() + "</td>");
                        out.println("<td>" + h.getActivo() + "</td>");
                        out.println("</tr>");
                    }
                    
                    out.println("</table>");
                }
            } else {
                out.println("<p style='color: red;'>⚠️ NO HAY USUARIO EN SESIÓN</p>");
                out.println("<p><a href='login.jsp'>Ir a login</a></p>");
            }
            
        } catch (Exception e) {
            out.println("<h3 style='color: red;'>❌ ERROR:</h3>");
            out.println("<pre>");
            e.printStackTrace(new java.io.PrintWriter(out));
            out.println("</pre>");
        }
    %>
    
    <hr>
    <p><a href="calendario?action=kanban">Ir al Kanban</a></p>
</body>
</html>
