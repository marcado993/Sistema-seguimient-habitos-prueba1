<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Hábitos</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
        }
        .welcome-container { 
            background: white; 
            padding: 60px; 
            border-radius: 15px; 
            box-shadow: 0 8px 32px rgba(0,0,0,0.1); 
        }
        .logo { 
            font-size: 4em; 
            margin-bottom: 20px; 
        }
        .btn { 
            padding: 15px 30px; 
            background: #2196F3; 
            color: white; 
            border: none; 
            border-radius: 8px; 
            cursor: pointer; 
            text-decoration: none; 
            display: inline-block;
            font-size: 18px;
            font-weight: bold;
            margin: 10px;
        }
        .btn:hover { 
            background: #1976D2; 
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <div class="logo">🎯</div>
        <h1>Sistema de Seguimiento de Hábitos</h1>
        <p style="color: #666; font-size: 18px; margin-bottom: 40px;">
            Construye mejores hábitos, seguimiento diario y alcanza tus objetivos
        </p>
        
        <a href="habitos?action=dashboard" class="btn">Ir al Dashboard</a>
        <a href="login.jsp" class="btn" style="background: #757575;">Login</a>
        <a href="diagnostico.jsp" class="btn" style="background: #FF9800;">Diagnóstico</a>
    </div>
</body>
</html>
