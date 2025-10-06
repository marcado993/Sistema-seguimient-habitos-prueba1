<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.User" %>
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
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<User> users = (List<User>) request.getAttribute("users");
                if (users != null && !users.isEmpty()) {
                    for (User u : users) {
            %>
            <tr>
                <td><%= u.getId() %></td>
                <td><%= u.getNombre() %></td>
                <td><%= u.getEmail() %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="3" style="text-align: center;">No hay usuarios registrados</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <div style="text-align: center; margin-top: 20px;">
        <a href="index.jsp">Volver al inicio</a>
    </div>
</body>
</html>