<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error - Sistema de Seguimiento</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #E9F7EF;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .error-container {
            background: white;
            padding: 3rem;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 500px;
        }
        h1 {
            color: #555555;
            font-size: 48px;
            margin-bottom: 1rem;
        }
        p {
            color: #888888;
            font-size: 16px;
            margin-bottom: 2rem;
        }
        .btn {
            display: inline-block;
            padding: 12px 24px;
            background: #A8E6CF;
            color: #555555;
            text-decoration: none;
            border-radius: 12px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn:hover {
            background: #90E0BC;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>😕 Oops!</h1>
        <p>
            <% 
                Integer errorCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
                if (errorCode != null && errorCode == 404) {
                    out.println("La página que buscas no existe.");
                } else {
                    out.println("Ha ocurrido un error inesperado.");
                }
            %>
        </p>
        <a href="${pageContext.request.contextPath}/login" class="btn">🏠 Ir al inicio</a>
    </div>
</body>
</html>
