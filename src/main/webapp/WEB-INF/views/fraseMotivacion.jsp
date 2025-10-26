<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String fraseActual = (String) request.getAttribute("fraseActual");
    Integer numeroFrase = (Integer) request.getAttribute("numeroFrase");
    String nombreUsuario = (String) session.getAttribute("nombre");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Frases Motivacionales - Sistema de Seguimiento</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: 'Poppins', sans-serif; 
            background: linear-gradient(135deg, #E9F7EF 0%, #FFE5E5 100%);
            min-height: 100vh; 
            padding: 20px; 
            color: #555;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .container { 
            max-width: 800px; 
            width: 100%;
            background: white;
            border-radius: 24px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
            padding: 3rem;
        }
        .header {
            text-align: center;
            margin-bottom: 2rem;
        }
        .header h1 {
            font-size: 32px;
            font-weight: 700;
            color: #555;
            margin-bottom: 10px;
        }
        .header p {
            font-size: 16px;
            color: #888;
            font-weight: 500;
        }
        .quote-card {
            background: linear-gradient(135deg, #A8E6CF20, #FFD6A520);
            border-radius: 16px;
            padding: 3rem 2rem;
            margin: 2rem 0;
            border-left: 5px solid #A8E6CF;
            position: relative;
            min-height: 200px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .quote-icon {
            position: absolute;
            top: 20px;
            left: 20px;
            font-size: 60px;
            opacity: 0.2;
        }
        .quote-text {
            font-size: 24px;
            line-height: 1.6;
            color: #555;
            font-weight: 500;
            font-style: italic;
            text-align: center;
            z-index: 1;
        }
        .quote-number {
            position: absolute;
            bottom: 15px;
            right: 20px;
            font-size: 14px;
            color: #888;
            font-weight: 600;
        }
        .controls {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }
        .btn {
            padding: 14px 32px;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            font-family: 'Poppins', sans-serif;
        }
        .btn-primary {
            background: linear-gradient(135deg, #A8E6CF, #FFD6A5);
            color: #555;
        }
        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(168, 230, 207, 0.4);
        }
        .btn-secondary {
            background: #f5f5f5;
            color: #555;
        }
        .btn-secondary:hover {
            background: #e0e0e0;
            transform: translateY(-2px);
        }
        .user-greeting {
            text-align: center;
            margin-bottom: 1rem;
            font-size: 18px;
            color: #666;
        }
        .divider {
            height: 2px;
            background: linear-gradient(90deg, transparent, #A8E6CF, transparent);
            margin: 2rem 0;
        }
        @media (max-width: 768px) {
            .container { padding: 2rem 1.5rem; }
            .quote-text { font-size: 20px; }
            .header h1 { font-size: 26px; }
            .controls { flex-direction: column; }
            .btn { width: 100%; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Frases Motivacionales</h1>
            <p>Encuentra tu inspiracion diaria</p>
        </div>
        
        <div class="user-greeting">
            Hola, <strong><%= nombreUsuario != null ? nombreUsuario : "Usuario" %></strong>
        </div>
        
        <div class="divider"></div>
        
        <div class="quote-card">
            <div class="quote-icon">💭</div>
            <div class="quote-text">
                "<%= fraseActual != null ? fraseActual : "Cargando frase..." %>"
            </div>
            <div class="quote-number">
                Frase <%= numeroFrase != null ? numeroFrase : 1 %> de 15
            </div>
        </div>
        
        <div class="controls">
            <a href="${pageContext.request.contextPath}/frases-motivacion?action=siguiente" class="btn btn-primary">
                Siguiente Frase
            </a>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">
                Volver al Dashboard
            </a>
        </div>
    </div>
</body>
</html>
