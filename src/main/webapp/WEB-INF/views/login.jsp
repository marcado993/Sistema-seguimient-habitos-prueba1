<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Sistema de Seguimiento</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', 'Segoe UI', sans-serif;
            background: #E9F7EF;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            max-width: 400px;
            width: 100%;
            animation: slideIn 0.5s ease-out;
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .login-header {
            background: linear-gradient(135deg, #A8E6CF 0%, #FFD6A5 100%);
            color: #555555;
            padding: 30px;
            text-align: center;
        }

        .login-header h1 {
            font-size: 28px;
            margin-bottom: 10px;
            font-weight: 700;
        }

        .login-header p {
            font-size: 14px;
            opacity: 0.85;
            color: #555555;
        }

        .login-body {
            padding: 40px 30px;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #F3E8FF;
            border-radius: 10px;
            font-size: 15px;
            transition: all 0.3s ease;
            background: #F3E8FF;
        }

        .form-group input:focus {
            outline: none;
            border-color: #FFD6A5;
            box-shadow: 0 0 0 3px rgba(255, 214, 165, 0.2);
            background: white;
        }

        .btn-login {
            width: 100%;
            padding: 14px;
            background: #A8E6CF;
            color: #555555;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(168, 230, 207, 0.4);
            background: #90E0BC;
        }

        .btn-login:active {
            transform: translateY(0);
        }

        .alert {
            padding: 12px 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-error {
            background-color: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }

        .alert-success {
            background-color: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }

        .register-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #e0e0e0;
        }

        .register-link a {
            color: #A8E6CF;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .register-link a:hover {
            color: #FFD6A5;
            text-decoration: underline;
        }

        .demo-credentials {
            background-color: #F3E8FF;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 13px;
        }

        .demo-credentials h4 {
            margin-bottom: 8px;
            color: #555555;
            font-weight: 600;
        }

        .demo-credentials p {
            margin: 4px 0;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🎯 Sistema de Seguimiento</h1>
            <p>Bienvenido de vuelta</p>
        </div>
        
        <div class="login-body">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    ⚠️ <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-success">
                    ✓ <%= request.getAttribute("mensaje") %>
                </div>
            <% } %>
            
            <div class="demo-credentials">
                <h4>👤 Credenciales de Demo:</h4>
                <p><strong>Correo:</strong> demo@ejemplo.com</p>
                <p><strong>Contraseña:</strong> demo123</p>
            </div>
            
            <form action="<%= request.getContextPath() %>/login" method="post">
                <div class="form-group">
                    <label for="correo">📧 Correo Electrónico</label>
                    <input type="email" id="correo" name="correo" 
                           placeholder="tu@correo.com" required>
                </div>
                
                <div class="form-group">
                    <label for="contrasena">🔒 Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" 
                           placeholder="••••••••" required>
                </div>
                
                <button type="submit" class="btn-login">
                    Iniciar Sesión
                </button>
            </form>
            
            <div class="register-link">
                ¿No tienes una cuenta? 
                <a href="<%= request.getContextPath() %>/registro">Regístrate aquí</a>
            </div>
        </div>
    </div>
</body>
</html>
