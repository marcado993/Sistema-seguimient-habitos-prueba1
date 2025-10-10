# 🐛 Guía de Debugging - Calendario Kanban

## Problema Reportado
Las tarjetas de hábitos no aparecen en la sección "Mis Hábitos Disponibles" del Kanban.

## ✅ Solución Implementada

### 1. **Logs de Debugging Agregados**

Abre la **Consola del Navegador** (F12 → Console) y verás estos mensajes:

```
📦 Hábitos del servidor: [array de hábitos]
🎯 Renderizando hábitos disponibles. Total: X
📋 Lista completa de hábitos: [detalles]
✅ Creando X tarjetas de hábitos...
  📌 Tarjeta 1: Nombre del hábito
  📌 Tarjeta 2: Nombre del hábito
  ...
```

### 2. **Verificaciones a Realizar**

#### ✓ **Paso 1: ¿Tienes hábitos creados?**
```
1. Ve a: http://localhost:8080/sistema-seguimiento
2. Login
3. Dashboard → "Mis Hábitos"
4. Verifica que existan hábitos en la tabla
```

#### ✓ **Paso 2: ¿El CalendarioController está cargando los hábitos?**
```java
// En CalendarioController.java línea ~65
List<Habito> habitosActivos = habitoDAO.findByUsuarioId(usuarioId);
request.setAttribute("habitos", habitosActivos);
```

Abre la consola del navegador y busca:
```
📦 Hábitos del servidor: []
```

- Si el array está **vacío `[]`**: No hay hábitos en la BD o el controller no los está cargando
- Si tiene **datos `[{id: 1, title: "..."}, ...]`**: Los hábitos llegaron correctamente

#### ✓ **Paso 3: ¿La sección se está renderizando?**

Busca en el HTML (F12 → Elements):
```html
<div id="habitos-disponibles">
  <!-- Aquí deben aparecer las tarjetas -->
</div>
```

### 3. **Posibles Causas del Problema**

| Problema | Causa | Solución |
|----------|-------|----------|
| **Array vacío `[]`** | No hay hábitos en la BD | Crear hábitos en "Mis Hábitos" |
| **`habitos = null`** | Controller no está pasando los datos | Verificar `CalendarioController.mostrarKanban()` |
| **Error de JSP** | Sintaxis incorrecta en el bucle | Verificar líneas 580-597 de kanban.jsp |
| **Error de JavaScript** | Función no se ejecuta | Abrir consola y buscar errores en rojo |

### 4. **Comandos de Prueba Rápida**

#### En la Consola del Navegador (F12):
```javascript
// Ver hábitos del servidor
console.log('Hábitos:', habitosDelServidor);

// Ver contenedor
console.log('Contenedor:', document.getElementById('habitos-disponibles'));

// Forzar render
renderHabitosDisponibles();
```

### 5. **Solución Temporal si no Aparecen**

Haz clic en el botón **"🔄 Sincronizar"** en la parte superior del Kanban.

Esto:
1. Recarga los hábitos desde `habitosDelServidor`
2. Limpia localStorage
3. Vuelve a renderizar todo

### 6. **Verificación de Datos en la BD**

Si sospechas que no hay hábitos en la BD, ejecuta en tu cliente SQL:

```sql
-- Ver todos los hábitos
SELECT * FROM habito;

-- Ver hábitos de un usuario específico
SELECT * FROM habito WHERE usuario_id = 'tu_usuario_id';
```

## 📸 Flujo Visual Esperado

```
┌─────────────────────────────────────┐
│  CALENDARIO KANBAN                 │
├─────────────────────────────────────┤
│  📚 MIS HÁBITOS DISPONIBLES        │
│  ┌──────┐ ┌──────┐ ┌──────┐       │
│  │ 🏃‍♂️  │ │ 📖   │ │ 💧   │       │
│  │Ejerc │ │Leer  │ │Agua  │       │
│  │ALTA  │ │MEDIA │ │MEDIA │       │
│  └──────┘ └──────┘ └──────┘       │
│   ↓ Arrastra al Kanban ↓          │
└─────────────────────────────────────┘
```

## 🆘 Si Nada Funciona

1. **Limpia localStorage**:
   ```javascript
   localStorage.removeItem('kanbanTasks');
   location.reload();
   ```

2. **Verifica la ruta**:
   ```
   URL actual: /calendario?action=kanban
   ```

3. **Revisa logs de Tomcat** para errores del servidor

4. **Compila y reinicia**:
   ```powershell
   mvn clean compile
   # Reinicia Tomcat
   ```

## 📞 Checklist Final

- [ ] Tengo hábitos creados en "Mis Hábitos"
- [ ] La consola muestra: `📦 Hábitos del servidor: [{...}]`
- [ ] No hay errores en rojo en la consola
- [ ] El div `habitos-disponibles` existe en el HTML
- [ ] He intentado el botón "🔄 Sincronizar"
- [ ] He limpiado localStorage y recargado

---

**Última actualización**: 2025-10-08
**Autor**: GitHub Copilot
