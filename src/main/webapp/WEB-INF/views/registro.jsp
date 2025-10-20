<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Sistema de Seguimiento</title>
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

        .registro-container {
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

        .registro-header {
            background: linear-gradient(135deg, #FFD6A5 0%, #F3E8FF 100%);
            color: #555555;
            padding: 30px;
            text-align: center;
        }

        .registro-header h1 {
            font-size: 28px;
            margin-bottom: 10px;
            font-weight: 700;
        }

        .registro-header p {
            font-size: 14px;
            opacity: 0.85;
            color: #555555;
        }

        .registro-body {
            padding: 40px 30px;
        }

        .form-group {
            margin-bottom: 20px;
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

        .btn-registro {
            width: 100%;
            padding: 14px;
            background: #FFD6A5;
            color: #555555;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-registro:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(255, 214, 165, 0.4);
            background: #FFC78C;
        }

        .btn-registro:active {
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

        .login-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #e0e0e0;
        }

        .login-link a {
            color: #FFD6A5;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .login-link a:hover {
            color: #A8E6CF;
            text-decoration: underline;
        }

        .password-requirements {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="registro-container">
        <div class="registro-header">
            <h1>📝 Crear Cuenta</h1>
            <p>Únete al Sistema de Seguimiento</p>
        </div>
        
        <div class="registro-body">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    ⚠️ <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form action="<%= request.getContextPath() %>/registro" method="post">
                <div class="form-group">
                    <label for="nombre">👤 Nombre Completo</label>
                    <input type="text" id="nombre" name="nombre" 
                           placeholder="Juan Pérez" required>
                </div>
                
                <div class="form-group">
                    <label for="correo">📧 Correo Electrónico</label>
                    <input type="email" id="correo" name="correo" 
                           placeholder="tu@correo.com" required>
                </div>
                
                <div class="form-group">
                    <label for="contrasena">🔒 Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" 
                           placeholder="••••••••" required minlength="6">
                    <div class="password-requirements">
                        Mínimo 6 caracteres
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="confirmarContrasena">🔒 Confirmar Contraseña</label>
                    <input type="password" id="confirmarContrasena" name="confirmarContrasena" 
                           placeholder="••••••••" required minlength="6">
                </div>
                
                <button type="submit" class="btn-registro">
                    Crear Cuenta
                </button>
            </form>
            
            <div class="login-link">
                ¿Ya tienes una cuenta? 
                <a href="<%= request.getContextPath() %>/login">Inicia sesión aquí</a>
            </div>
        </div>
    </div>
</body>
</html>
