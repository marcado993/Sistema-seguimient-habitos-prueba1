# 10 Refactorizaciones para Sistema de Seguimiento de Hábitos
## Práctica XP - Refactoring

Basado en la arquitectura MVC actual del proyecto y los diagramas proporcionados, aquí están 10 refactorizaciones que mejorarán la calidad del código:

---

## 1. **Extract Interface - Crear Interfaces para DAOs**
**Dificultad:** ⭐⭐

**Problema:** Los DAOs están acoplados directamente sin contratos definidos.

**Refactorización:**
- Crear interfaces `IHabitoDAO`, `IObjetivoDAO`, `IUserDAO` en carpeta `dao/`
- Implementar interfaces en las clases DAO existentes
- Usar inyección de dependencias en controladores

**Beneficio:** Mejora testabilidad y permite intercambiar implementaciones.

```java
// Crear en: src/main/java/com/example/dao/IHabitoDAO.java
public interface IHabitoDAO {
    List<Habito> findByUsuarioId(String usuarioId);
    Optional<Habito> findById(Long id);
    Habito save(Habito habito);
    void delete(Long id);
}
```

---

## 2. **Extract Service Layer - Capa de Servicios**
**Dificultad:** ⭐⭐⭐

**Problema:** Los controladores contienen lógica de negocio mezclada con lógica de presentación.

**Refactorización:**

- Crear carpeta `service/` 
- Crear clases: `HabitoService`, `ObjetivoService`, `SeguimientoService`
- Mover lógica de negocio desde controladores a servicios

**Beneficio:** Separación clara de responsabilidades según MVC.

```java
// Crear: src/main/java/com/example/service/HabitoService.java
public class HabitoService {
    private IHabitoDAO habitoDAO;
    
    public HabitoService(IHabitoDAO habitoDAO) {
        this.habitoDAO = habitoDAO;
    }
    
    public List<Habito> obtenerHabitosActivos(String usuarioId) {
        // Lógica de negocio aquí
    }
}
```

---

## 3. **Replace Magic Numbers - Eliminar Números Mágicos**
**Dificultad:** ⭐

**Problema:** Uso de números literales en el código sin contexto.

**Refactorización:**
- Crear clase `Constants` en package `utils/`
- Definir constantes para códigos HTTP, límites, timeouts
- Reemplazar números mágicos en DAOs y controladores

**Beneficio:** Código más legible y mantenible.

```java
// Crear: src/main/java/com/example/utils/Constants.java
public class Constants {
    public static final int MAX_NOMBRE_LENGTH = 200;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String SESSION_USER_ID = "usuarioId";
    public static final String DEMO_USER = "demo";
}
```

---

## 4. **Extract Method - Dividir Métodos Largos**
**Dificultad:** ⭐⭐

**Problema:** Métodos en controladores y DAOs son muy largos (ej: `doGet`, `doPost`).

**Refactorización:**
- Dividir `HabitoController.doGet()` en métodos pequeños
- Crear métodos privados para cada acción específica
- Aplicar en todos los controladores

**Beneficio:** Métodos más legibles y reutilizables.

```java
// En HabitoController
private void procesarAccionListar(HttpServletRequest request, HttpServletResponse response, String usuarioId) {
    // Lógica específica para listar
}

private void procesarAccionNuevo(HttpServletRequest request, HttpServletResponse response) {
    // Lógica específica para nuevo
}
```

---

## 5. **Introduce Parameter Object - DTO para Transferencia**
**Dificultad:** ⭐⭐

**Problema:** Métodos con muchos parámetros primitivos.

**Refactorización:**

- Crear DTOs en carpeta `model/`: `HabitoDTO`, `ObjetivoDTO`, `RegistroProgresoDTO`
- Usar DTOs en lugar de múltiples parámetros

**Beneficio:** Menos parámetros, mejor encapsulación.

```java
// Crear: src/main/java/com/example/model/HabitoDTO.java
public class HabitoDTO {
    private String nombre;
    private String descripcion;
    private String frecuencia;
    private Integer metaDiaria;
    
    // Constructor, getters y setters
}
```

---

## 6. **Replace Exception with Test - Validaciones Preventivas**
**Dificultad:** ⭐⭐

**Problema:** Uso excesivo de try-catch para control de flujo.

**Refactorización:**
- Crear clase `ValidationUtils` en package `utils/`
- Implementar validaciones antes de operaciones riesgosas
- Reducir dependencia de excepciones para lógica de negocio

**Beneficio:** Código más eficiente y predecible.

```java
// Crear: src/main/java/com/example/utils/ValidationUtils.java
public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
    
    public static boolean isValidHabitoData(String nombre, String frecuencia) {
        return nombre != null && !nombre.trim().isEmpty() && frecuencia != null;
    }
}
```

---

## 7. **Extract Superclass - Controlador Base**
**Dificultad:** ⭐⭐⭐

**Problema:** Código duplicado en todos los controladores.

**Refactorización:**

- Crear `BaseController` en carpeta `servlet/`
- Mover funcionalidad común (validación de sesión, manejo de errores)
- Hacer que todos los controladores hereden de `BaseController`

**Beneficio:** Eliminación de duplicación, mantenimiento centralizado.

```java
// Crear: src/main/java/com/example/servlet/BaseController.java
public abstract class BaseController extends HttpServlet {
    protected String validateSession(HttpServletRequest request) {
        // Lógica común de validación de sesión
    }
    
    protected void handleError(HttpServletRequest request, HttpServletResponse response, String error) {
        // Manejo común de errores
    }
}
```

---

## 8. **Replace Conditional with Polymorphism - Strategy Pattern**
**Dificultad:** ⭐⭐⭐⭐

**Problema:** Múltiples `switch/if` para manejar diferentes tipos de frecuencia.

**Refactorización:**

- Crear interface `FrecuenciaStrategy` en carpeta `utils/`
- Implementar: `DiarioStrategy`, `SemanalStrategy`, `MensualStrategy`
- Eliminar enum `FrecuenciaHabito` y usar polimorfismo

**Beneficio:** Código extensible, fácil agregar nuevas frecuencias.

```java
// Crear: src/main/java/com/example/utils/FrecuenciaStrategy.java
public interface FrecuenciaStrategy {
    boolean debeEjecutarseHoy(LocalDate fechaInicio, LocalDate fechaActual);
    int calcularRachaMaxima(List<LocalDate> fechasCumplimiento);
}
```

---

## 9. **Move Method - Reorganizar Responsabilidades**
**Dificultad:** ⭐⭐

**Problema:** Métodos de cálculo están en lugares incorrectos.

**Refactorización:**
- Mover `calcularRachaActual()` de `HabitoDAO` a `Habito` (modelo)
- Mover validaciones de `Controller` a `Service`
- Crear `CalculadoraProgreso` en package `utils/`

**Beneficio:** Responsabilidades en el lugar correcto.

```java
// En la clase Habito
public int calcularRachaActual() {
    // Lógica movida desde DAO
    return this.registros.stream()
        .mapToInt(this::calcularRachaDiaria)
        .sum();
}
```

---

## 10. **Introduce Factory - Factory para EntityManager**
**Dificultad:** ⭐⭐

**Problema:** Creación repetitiva y acoplada de EntityManager.

**Refactorización:**
- Refactorizar `EntityManagerUtil` como Factory Pattern
- Crear `DAOFactory` para crear instancias de DAO
- Implementar pool de conexiones

**Beneficio:** Mejor gestión de recursos, configuración centralizada.

```java
// Refactorizar: src/main/java/com/example/dao/EntityManagerUtil.java
public class EntityManagerFactory {
    private static EntityManagerFactory instance;
    
    public static EntityManagerFactory getInstance() {
        if (instance == null) {
            instance = new EntityManagerFactory();
        }
        return instance;
    }
    
    public EntityManager createEntityManager() {
        // Lógica mejorada de creación
    }
}
```

---

## 📋 Plan de Ejecución Recomendado

1. **Fase 1 (Fácil):** Refactorizaciones 3, 4, 6
2. **Fase 2 (Intermedio):** Refactorizaciones 1, 2, 5, 9, 10
3. **Fase 3 (Avanzado):** Refactorizaciones 7, 8

## 🎯 Estructura Final Esperada

```
src/main/java/com/example/
├── dao/
│   ├── IHabitoDAO.java (nuevo)
│   ├── IObjetivoDAO.java (nuevo)
│   ├── IUserDAO.java (nuevo)
│   └── HabitoDAO.java (refactorizado)
├── listener/ (para eventos)
├── model/
│   ├── Habito.java (refactorizado)
│   ├── HabitoDTO.java (nuevo)
│   └── ...
├── service/ (nuevo)
│   ├── HabitoService.java
│   ├── ObjetivoService.java
│   └── SeguimientoService.java
├── servlet/
│   ├── BaseController.java (nuevo)
│   └── HabitoController.java (refactorizado)
└── utils/ (nuevo)
    ├── Constants.java
    ├── ValidationUtils.java
    ├── CalculadoraProgreso.java
    └── FrecuenciaStrategy.java
```

## ✅ Criterios de Éxito

- [ ] Métodos con menos de 20 líneas
- [ ] Eliminación de código duplicado
- [ ] Separación clara de responsabilidades MVC
- [ ] Fácil testing unitario
- [ ] Código más legible y mantenible

¡Buena suerte con tu práctica XP! 🚀
