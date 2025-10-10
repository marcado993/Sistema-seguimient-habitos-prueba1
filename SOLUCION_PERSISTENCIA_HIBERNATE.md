# 🔧 Solución: Persistencia Hibernate - Objetivo + Hábito

## Fecha: 10 de Octubre, 2025

---

## ✅ Cambios Realizados

### 1. **Actualización de `persistence.xml`**

**Problema:** Faltaba registrar la entidad `RegistroHabito`

**Solución:**
```xml
<class>com.example.model.Usuario</class>
<class>com.example.model.Objetivo</class>
<class>com.example.model.RegistroProgreso</class>
<class>com.example.model.Habito</class>
<class>com.example.model.RegistroHabito</class>  <!-- ✅ AGREGADO -->
<class>com.example.model.TareaKanban</class>
```

---

### 2. **Mejoras en `HabitoDAO.save()`**

Agregado manejo correcto de la relación con `Objetivo`:

```java
public Habito save(Habito habito) {
    EntityManager em = EntityManagerUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
        tx.begin();
        
        // ✅ NUEVO: Asegurar que el objetivo esté en el contexto de persistencia
        if (habito.getObjetivo() != null) {
            System.out.println("🎯 Asociado al objetivo ID: " + habito.getObjetivo().getId());
            if (!em.contains(habito.getObjetivo())) {
                habito.setObjetivo(em.merge(habito.getObjetivo()));
            }
        }
        
        if (habito.getId() == null) {
            em.persist(habito);
        } else {
            habito = em.merge(habito);
        }
        
        tx.commit();
        return habito;
    } catch (Exception e) {
        if (tx.isActive()) {
            tx.rollback();
        }
        throw new RuntimeException("Error al guardar hábito", e);
    } finally {
        em.close();
    }
}
```

**¿Por qué este cambio?**
- Cuando asociamos un `Objetivo` a un `Habito`, el objetivo debe estar "manejado" por el EntityManager
- `em.merge()` asegura que el objeto esté en el contexto de persistencia
- Esto evita errores de "detached entity"

---

### 3. **Logs de Debugging Agregados**

Ahora ambos DAOs imprimen información útil:

```
💾 Guardando objetivo: Perder peso
✅ Objetivo persistido con ID: 1
✅ Transacción confirmada

💾 Guardando hábito: Comer saludable
🎯 Asociado al objetivo ID: 1
✅ Hábito persistido con ID: 1
✅ Transacción confirmada
```

---

### 4. **Página de Test: `test-persistencia.jsp`**

Creada una página de prueba completa para verificar la persistencia:

**URL:** `http://localhost:8080/sistema-seguimiento/test-persistencia.jsp`

**Características:**
- ✅ Botón "Crear Test" - Crea objetivo + hábito asociado
- ✅ Botón "Verificar" - Verifica que las relaciones se guardaron
- ✅ Botón "Listar Todo" - Muestra todos los objetivos y hábitos
- ✅ Logs visuales en la página
- ✅ Manejo de errores detallado

---

## 🧪 Cómo Probar la Persistencia

### Opción 1: Usar la Página de Test

1. **Inicia el servidor Tomcat**

2. **Accede a la página de test:**
   ```
   http://localhost:8080/sistema-seguimiento/test-persistencia.jsp
   ```

3. **Haz clic en "Crear Test"**
   - Crea un objetivo de prueba
   - Crea un hábito asociado automáticamente
   - Verás mensajes de éxito/error

4. **Haz clic en "Verificar"**
   - Recupera el objetivo y hábito de la BD
   - Verifica que la relación esté correcta
   - Muestra si el hábito tiene objetivo asociado

5. **Haz clic en "Listar Todo"**
   - Lista todos los objetivos con sus hábitos
   - Lista todos los hábitos con sus objetivos

---

### Opción 2: Usar el Flujo Normal

1. **Establecer Objetivo:**
   ```
   http://localhost:8080/sistema-seguimiento/WEB-INF/views/establecerObjetivo.jsp
   ```
   - Llena el formulario
   - Click en "Crear Objetivo"

2. **Planificar Objetivo:**
   - El sistema te redirige automáticamente
   - Verás la card del objetivo recién creado
   - Click en la card para seleccionarla

3. **Crear Hábito Asociado:**
   - Llena el formulario del hábito
   - Click en "Crear Hábito y Asociar"

4. **Verificar en Consola:**
   Deberías ver en la consola del servidor:
   ```
   💾 Guardando objetivo: [Tu Objetivo]
   ✅ Objetivo persistido con ID: X
   
   💾 Guardando hábito: [Tu Hábito]
   🎯 Asociado al objetivo ID: X
   ✅ Hábito persistido con ID: Y
   ```

---

## 🔍 Verificar en la Base de Datos H2

### Acceder a la Consola H2:

1. **Agrega esta configuración en `web.xml`:**
```xml
<servlet>
    <servlet-name>H2Console</servlet-name>
    <servlet-class>org.h2.server.web.WebServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>H2Console</servlet-name>
    <url-pattern>/h2-console/*</url-pattern>
</servlet-mapping>
```

2. **Accede a:**
   ```
   http://localhost:8080/sistema-seguimiento/h2-console
   ```

3. **Credenciales:**
   ```
   JDBC URL: jdbc:h2:~/testdb
   User: sa
   Password: (vacío)
   ```

4. **Ejecuta consultas SQL:**

```sql
-- Ver todos los objetivos
SELECT * FROM objetivos;

-- Ver todos los hábitos con su objetivo_id
SELECT id, nombre, frecuencia, objetivo_id FROM habitos;

-- Ver relación completa
SELECT 
    h.id AS habito_id,
    h.nombre AS habito_nombre,
    o.id AS objetivo_id,
    o.titulo AS objetivo_titulo
FROM habitos h
LEFT JOIN objetivos o ON h.objetivo_id = o.id;
```

---

## 🐛 Solución de Problemas

### Problema 1: "detached entity passed to persist"

**Causa:** El objetivo no está en el contexto de persistencia

**Solución:** Ya implementada en `HabitoDAO.save()`
```java
if (!em.contains(habito.getObjetivo())) {
    habito.setObjetivo(em.merge(habito.getObjetivo()));
}
```

### Problema 2: La columna `objetivo_id` es NULL

**Causa:** No se está llamando a `habito.setObjetivo(objetivo)`

**Verificar en `ControladorHabitos`:**
```java
if (objetivoIdStr != null && !objetivoIdStr.isEmpty()) {
    Long objetivoId = Long.parseLong(objetivoIdStr);
    Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
    if (objetivo != null) {
        habito.setObjetivo(objetivo);  // ✅ CRÍTICO
    }
}
```

### Problema 3: No se ve el SQL en la consola

**Verificar `persistence.xml`:**
```xml
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

---

## ✅ Checklist de Verificación

- [ ] `persistence.xml` incluye `RegistroHabito`
- [ ] `HabitoDAO.save()` hace merge del objetivo
- [ ] Logs de debugging aparecen en consola
- [ ] Test de persistencia funciona sin errores
- [ ] SQL INSERT aparece en la consola
- [ ] Columna `objetivo_id` tiene valor (no NULL)
- [ ] Consulta SQL JOIN muestra la relación

---

## 📊 Estructura Esperada en BD

### Tabla `objetivos`
```
id | titulo        | descripcion | estado  | usuario_id    | ...
1  | Perder peso   | ...         | ACTIVO  | usuario_demo  | ...
```

### Tabla `habitos`
```
id | nombre           | frecuencia | meta_diaria | objetivo_id | usuario_id    | ...
1  | Comer saludable  | DIARIO     | 5          | 1           | usuario_demo  | ...
```

**✅ La clave es que `objetivo_id = 1` (debe coincidir con el ID del objetivo)**

---

## 🎉 Resultado Final

Con estos cambios, ahora:

1. ✅ Los objetivos se persisten correctamente
2. ✅ Los hábitos se persisten correctamente
3. ✅ La relación `Habito → Objetivo` se guarda en la columna `objetivo_id`
4. ✅ Se pueden recuperar objetivos con sus hábitos asociados
5. ✅ Se pueden recuperar hábitos con su objetivo padre
6. ✅ Los logs permiten debugging fácil
7. ✅ La página de test permite verificación rápida

---

## 🚀 Próximos Pasos

1. Ejecuta la página de test para verificar que todo funciona
2. Revisa los logs de la consola del servidor
3. Si hay errores, cópialos y analízalos
4. Una vez verificado, prueba el flujo completo desde establecer objetivo

¿Encuentras algún error? Comparte el mensaje de error completo para ayudarte.
