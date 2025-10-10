<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Usuario" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lista de Usuarios</title>
    <style>
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        h1 {
            text-align: center;
            color: #333;
        }
    </style>
</head>
<body>
    <h1>Lista de Usuarios</h1>

    <table>
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Correo</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuario usuario : usuarios) {
            %>
            <tr>
                <td><%= usuario.getNombre() %></td>
                <td><%= usuario.getCorreo() %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="2" style="text-align: center;">No hay usuarios registrados</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <div style="text-align: center; margin-top: 20px;">
        <a href="dashboard.jsp">Volver al Dashboard</a>
    </div>
</body>
</html>
