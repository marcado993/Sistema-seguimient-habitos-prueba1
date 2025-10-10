# 🎯 Asociación de Objetivos y Hábitos

## Fecha: 10 de Octubre, 2025

---

## ✅ Flujo Implementado

### 1. **Establecer Objetivo**
**Vista:** `establecerObjetivo.jsp`
**Controlador:** `ControladorObjetivo` (POST action="crear")

El usuario llena el formulario:
- Título del objetivo (ej: "Perder peso")
- Descripción
- Fecha de inicio
- Fecha de finalización
- Estado inicial

Al hacer clic en **"Crear Objetivo"**, se ejecuta:

```java
POST /controlador-objetivos?action=crear
```

---

### 2. **Redirección a Planificar Objetivo**
**Vista:** `planificarObjetivo.jsp`

Después de crear el objetivo, el sistema automáticamente:
1. Guarda el objetivo en la base de datos
2. Redirige a `planificarObjetivo.jsp`
3. Muestra **cards con todos los objetivos** del usuario

```java
// En ControladorObjetivo.java
if (nuevoObjetivo != null) {
    request.setAttribute("objetivos", objetivoDAO.findByUsuarioId(usuarioId));
    request.getRequestDispatcher("/WEB-INF/views/planificarObjetivo.jsp").forward(request, response);
}
```

---

### 3. **Seleccionar Objetivo y Crear Hábito Asociado**

#### 3.1 **Cards de Objetivos**
Cada objetivo se muestra en una card con:
- 🎯 Título del objetivo
- Descripción
- 📅 Fecha de creación
- ⏰ Fecha límite

#### 3.2 **Seleccionar Objetivo**
Al hacer clic en una card:
- Se marca visualmente (borde verde)
- Se habilita el formulario de hábito
- Se guarda el `objetivoId` en un campo oculto

#### 3.3 **Crear Hábito Asociado**
El usuario llena:
- **Nombre del hábito** (ej: "Comer saludable")
- **Descripción**
- **Meta diaria** (número de veces por día)
- **Frecuencia:** Diario / Semanal / Mensual
- **Fecha de inicio**

Al hacer clic en **"Crear Hábito y Asociar"**:

```java
POST /controlador-habitos?action=crear-con-objetivo
Parámetros:
- nombre: "Comer saludable"
- frecuencia: "SEMANAL"
- metaDiaria: 5
- objetivoId: 1
- fechaInicio: "2025-10-10"
```

---

### 4. **Guardado en Base de Datos**

El sistema:
1. Crea el hábito con todos sus datos
2. **Asocia el hábito al objetivo** mediante la relación `@ManyToOne`
3. Guarda en la base de datos

```java
// En ControladorHabitos.java
if (objetivoIdStr != null && !objetivoIdStr.isEmpty()) {
    Long objetivoId = Long.parseLong(objetivoIdStr);
    Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
    if (objetivo != null) {
        habito.setObjetivo(objetivo);
    }
}
```

---

## 📊 Modelo de Datos

### Relaciones JPA

**Objetivo.java**
```java
@OneToMany(mappedBy = "objetivo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Habito> habitos = new ArrayList<>();

public void agregarHabito(Habito habito) {
    if (habito != null) {
        habito.setObjetivo(this);
        this.habitos.add(habito);
    }
}
```

**Habito.java**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "objetivo_id")
private Objetivo objetivo;

public void setObjetivo(Objetivo objetivo) {
    this.objetivo = objetivo;
}
```

---

## 🎨 Características de UX/UI

### Cards Interactivas de Objetivos
- ✅ Diseño con gradiente `#FFD6A5` → `#FFC78C`
- ✅ Hover effect con elevación
- ✅ Selección visual con borde verde `#A8E6CF`
- ✅ Scroll suave al formulario al seleccionar

### Validaciones
- ⚠️ No permite enviar sin seleccionar un objetivo
- ⚠️ Botón deshabilitado hasta seleccionar objetivo
- ⚠️ Alerta si intenta enviar sin selección

---

## 📝 Ejemplo Completo

### Caso de Uso: "Perder peso"

1. **Usuario establece objetivo:**
   - Título: "Perder peso"
   - Descripción: "Bajar 5kg en 3 meses"
   - Fecha límite: 10/01/2026

2. **Sistema redirige a planificar:**
   - Muestra card del objetivo "Perder peso"

3. **Usuario hace clic en la card:**
   - Card se marca como seleccionada
   - Aparece formulario de hábito

4. **Usuario crea hábito:**
   - Nombre: "Comer saludable"
   - Meta: 5 comidas saludables por semana
   - Frecuencia: SEMANAL

5. **Sistema guarda:**
   ```
   Habito {
     nombre: "Comer saludable",
     frecuencia: SEMANAL,
     metaDiaria: 5,
     objetivo_id: 1 (Perder peso)
   }
   ```

6. **Resultado:**
   - Hábito "Comer saludable" queda asociado a "Perder peso"
   - Ahora el usuario puede registrar su progreso con `RegistroHabito`

---

## 🔄 Próximos Pasos

### Registrar Progreso del Hábito

Una vez creado el hábito asociado, el usuario puede:

1. Ir a **"Registrar Progreso"** (`registroHabito.jsp`)
2. Seleccionar el hábito "Comer saludable"
3. Registrar cuántas veces lo cumplió (ej: 5/5)
4. El progreso se guarda en `RegistroHabito`
5. El sistema actualiza automáticamente:
   - Racha del hábito
   - Porcentaje de completado
   - Progreso del objetivo padre

---

## 🎯 Beneficios de esta Implementación

1. ✅ **Trazabilidad:** Cada hábito sabe a qué objetivo pertenece
2. ✅ **Organización:** Los objetivos agrupan múltiples hábitos
3. ✅ **Métricas:** Se puede calcular el progreso del objetivo sumando el de sus hábitos
4. ✅ **Motivación:** El usuario ve cómo sus hábitos contribuyen al objetivo mayor
5. ✅ **Flexibilidad:** Un objetivo puede tener múltiples hábitos asociados

---

## 🗃️ Estructura de Tablas

### Tabla `objetivos`
```sql
id | titulo        | descripcion           | fecha_creacion | fecha_limite | progreso | usuario_id
1  | Perder peso   | Bajar 5kg en 3 meses | 2025-10-10     | 2026-01-10  | 0        | usuario_demo
```

### Tabla `habitos`
```sql
id | nombre           | frecuencia | meta_diaria | objetivo_id | usuario_id
1  | Comer saludable  | SEMANAL    | 5          | 1           | usuario_demo
2  | Hacer ejercicio  | DIARIO     | 1          | 1           | usuario_demo
```

### Tabla `registros_habito`
```sql
id | habito_id | fecha      | completado | observacion
1  | 1         | 2025-10-10 | 5         | Cumplí perfectamente
2  | 1         | 2025-10-11 | 4         | Casi lo logro
```

---

## 🎉 Conclusión

El sistema ahora permite:
1. ✅ Establecer objetivos generales
2. ✅ Asociar múltiples hábitos a un objetivo
3. ✅ Registrar el progreso de cada hábito
4. ✅ Visualizar cómo los hábitos contribuyen al objetivo

**Flujo completo:**
Objetivo → Hábitos → Registros → Progreso → Cumplimiento del Objetivo
