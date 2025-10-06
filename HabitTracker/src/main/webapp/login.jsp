<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistema de Hábitos</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container { 
            background: white; 
            padding: 40px; 
            border-radius: 10px; 
            box-shadow: 0 4px 20px rgba(0,0,0,0.1); 
            width: 100%;
            max-width: 400px;
        }
        .logo { 
            text-align: center; 
            margin-bottom: 30px; 
            color: #2196F3;
        }
        .form-group { 
            margin-bottom: 20px; 
        }
        .form-group label { 
            display: block; 
            margin-bottom: 5px; 
            font-weight: bold; 
        }
        .form-group input { 
            width: 100%; 
            padding: 12px; 
            border: 1px solid #ddd; 
            border-radius: 5px; 
            box-sizing: border-box;
            font-size: 16px;
        }
        .btn { 
            width: 100%;
            padding: 12px; 
            background: #2196F3; 
            color: white; 
            border: none; 
            border-radius: 5px; 
            cursor: pointer; 
            font-size: 16px;
            font-weight: bold;
        }
        .btn:hover { 
            background: #1976D2; 
        }
        .welcome-text {
            text-align: center;
            color: #666;
            margin-bottom: 20px;
        }
        .demo-info {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 14px;
            color: #1976d2;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <h1>🎯 Hábitos</h1>
            <h2>Sistema de Seguimiento</h2>
        </div>
        
        <div class="welcome-text">
            <p>Bienvenido a tu sistema personal de seguimiento de hábitos</p>
        </div>
        
        <form method="post" action="login">
            <div class="form-group">
                <label for="usuario">Usuario:</label>
                <input type="text" 
                       id="usuario" 
                       name="usuario" 
                       placeholder="Ingresa tu usuario"
                       value="demo"
                       required>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="Ingresa tu contraseña"
                       value="demo123"
                       required>
            </div>
            
            <button type="submit" class="btn">Iniciar Sesión</button>
        </form>
        
        <div class="demo-info">
            <strong>Demo del Sistema:</strong><br>
            Usuario: demo<br>
            Contraseña: demo123<br>
            <em>¡Prueba el sistema con estas credenciales!</em>
        </div>
        
        <div style="text-align: center; margin-top: 20px; color: #666;">
            <p><a href="habitos?action=dashboard" style="color: #2196F3;">Acceso directo al Dashboard</a></p>
        </div>
    </div>
</body>
</html>
