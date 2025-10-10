<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Editar' : 'Crear'} Hábito - Sistema de Hábitos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #2196F3; color: white; padding: 20px; text-align: center; }
        .form-container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select, .form-group textarea { 
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ddd; 
            border-radius: 4px; 
            box-sizing: border-box; 
        }
        .form-group textarea { height: 80px; resize: vertical; }
        .btn { 
            padding: 12px 24px; 
            background: #2196F3; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
            text-decoration: none; 
            display: inline-block; 
            margin-right: 10px;
        }
        .btn:hover { background: #1976D2; }
        .btn-secondary { background: #757575; }
        .btn-secondary:hover { background: #616161; }
        .actions { text-align: center; margin-top: 30px; }
    </style>
</head>
<body>
    <div class="header">
        <h1>${isEdit ? 'Editar' : 'Crear'} Hábito</h1>
        <p>${isEdit ? 'Modifica los datos de tu hábito' : 'Crea un nuevo hábito para seguir'}</p>
    </div>
    
    <div class="container">
        <div class="form-container">
            <form method="post" action="habitos">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${habito.id}">
                </c:if>
                
                <div class="form-group">
                    <label for="nombre">Nombre del Hábito *</label>
                    <input type="text" 
                           id="nombre" 
                           name="nombre" 
                           value="${habito.nombre}" 
                           placeholder="Ej: Hacer ejercicio, Leer libros, Meditar..."
                           required>
                </div>
                
                <div class="form-group">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" 
                              name="descripcion" 
                              placeholder="Describe tu hábito y por qué es importante para ti...">${habito.descripcion}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="frecuencia">Frecuencia *</label>
                    <select id="frecuencia" name="frecuencia" required>
                        <option value="">Selecciona la frecuencia</option>
                        <option value="DIARIO" ${habito.frecuencia == 'DIARIO' ? 'selected' : ''}>Diario</option>
                        <option value="SEMANAL" ${habito.frecuencia == 'SEMANAL' ? 'selected' : ''}>Semanal</option>
                        <option value="MENSUAL" ${habito.frecuencia == 'MENSUAL' ? 'selected' : ''}>Mensual</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="metaDiaria">Meta Diaria *</label>
                    <input type="number" 
                           id="metaDiaria" 
                           name="metaDiaria" 
                           value="${habito.metaDiaria}" 
                           min="1" 
                           max="100" 
                           placeholder="¿Cuántas veces por día?"
                           required>
                    <small style="color: #666;">Número de veces que quieres realizar este hábito por día</small>
                </div>
                
                <div class="actions">
                    <button type="submit" class="btn">
                        ${isEdit ? 'Actualizar' : 'Crear'} Hábito
                    </button>
                    <a href="habitos?action=list" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
        
        <!-- Información adicional -->
        <div style="background: white; padding: 20px; border-radius: 8px; margin: 20px 0;">
            <h3>Consejos para crear hábitos exitosos:</h3>
            <ul>
                <li><strong>Sé específico:</strong> Define exactamente qué vas a hacer</li>
                <li><strong>Empieza pequeño:</strong> Es mejor hacer poco consistentemente que mucho esporádicamente</li>
                <li><strong>Vincúlalo a algo existente:</strong> Conecta tu nuevo hábito con una rutina establecida</li>
                <li><strong>Sé realista:</strong> Elige metas alcanzables para mantener la motivación</li>
            </ul>
        </div>
    </div>
</body>
</html>
