# Informe de Refactorización - Extreme Programming
## Sistema de Seguimiento de Hábitos

**Estudiante:** [Tu Nombre]  
**Fecha:** 8 de Octubre, 2025  
**Asignatura:** Extreme Programming - Refactorización  

---

## Objetivo

El objetivo de este informe es ilustrar de manera clara y precisa cómo la refactorización puede mejorar la calidad del software, promoviendo prácticas de desarrollo más robustas, sostenibles y eficientes. Con cada refactorización, se demuestra cómo se optimiza el código para reducir complejidad, eliminar redundancias, y facilitar su comprensión y mantenimiento a largo plazo.

---

## Introducción

Este informe detalla el proceso de refactorización aplicado a varios métodos en el **Sistema de Seguimiento de Hábitos** con el objetivo de mejorar su legibilidad, mantenibilidad y eficiencia, sin alterar el comportamiento funcional del sistema. La refactorización es una técnica clave en el desarrollo de software que permite optimizar el código existente, haciéndolo más limpio y fácil de entender, a la vez que facilita su mantenimiento y posibles futuras modificaciones.

Para cada refactorización realizada en este proyecto, se documenta el estado del código antes del cambio, los pasos detallados que se siguieron para implementar la refactorización, y el resultado final del código después de la mejora. Además, se proporciona una breve explicación sobre las razones que justifican cada refactorización, destacando cómo estos cambios contribuyen a una mejor estructura, organización, y reutilización del código.

---

## Desarrollo

### 1. Extract Method en HabitoController.java

**Código antes de la refactorización**

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");
    HttpSession session = request.getSession();
    String usuarioId = (String) session.getAttribute("usuarioId");
    
    if (usuarioId == null) {
        session.setAttribute("usuarioId", "demo");
        session.setAttribute("usuarioNombre", "Usuario Demo");
        usuarioId = "demo";
    }
    
    try {
        switch (action != null ? action : "list") {
            case "list":
                listarHabitos(request, response, usuarioId);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                String idStr = request.getParameter("id");
                Long id = Long.parseLong(idStr);
                mostrarFormularioEditar(request, response, id);
                break;
            case "delete":
                String deleteIdStr = request.getParameter("id");
                Long deleteId = Long.parseLong(deleteIdStr);
                eliminarHabito(request, response, deleteId);
                break;
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }
}
```

**Pasos de la refactorización**

1. Identificar el refactoring a realizar: **Extract Method**
2. Se identifica el fragmento de código que valida y crea la sesión del usuario
3. En IntelliJ, seleccionar las líneas de validación de sesión (líneas 7-13)
4. Presionar `Ctrl+Alt+M` (Windows) o `Cmd+Alt+M` (Mac) para extraer el método
5. Nombrar el método como `obtenerOCrearUsuarioId()`
6. Reemplazar el código original por la llamada al nuevo método
7. Realizar pruebas para verificar que no se haya modificado su comportamiento

**Código después de la refactorización**

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");
    String usuarioId = obtenerOCrearUsuarioId(request);
    
    try {
        switch (action != null ? action : "list") {
            case "list":
                listarHabitos(request, response, usuarioId);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response, Long.parseLong(request.getParameter("id")));
                break;
            case "delete":
                eliminarHabito(request, response, Long.parseLong(request.getParameter("id")));
                break;
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }
}

private String obtenerOCrearUsuarioId(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String usuarioId = (String) session.getAttribute("usuarioId");
    
    if (usuarioId == null) {
        session.setAttribute("usuarioId", "demo");
        session.setAttribute("usuarioNombre", "Usuario Demo");
        usuarioId = "demo";
    }
    
    return usuarioId;
}
```

Este método separa la lógica de validación de sesión en un método independiente. Esto permite que el método principal `doGet()` se enfoque solo en el enrutamiento de acciones, mientras que la gestión de sesión se encapsula en `obtenerOCrearUsuarioId()`, mejorando la legibilidad y reutilización del código.

---

### 2. Inline Variable en ObjetivoController.java

**Código antes de la refactorización**

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    String usuarioId = (String) session.getAttribute("usuarioId");
    
    String action = request.getParameter("action");
    
    if ("crear".equals(action)) {
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");
        String frecuencia = request.getParameter("frecuencia");
        
        Objetivo nuevoObjetivo = crear(nombre, categoria, frecuencia);
        
        if (nuevoObjetivo != null) {
            String mensaje = notificarExitoRegistro(nuevoObjetivo);
            limpiarFormulario(request);
            response.sendRedirect("controlador-objetivos?action=listar&success=true");
        }
    }
}
```

**Pasos de la refactorización**

1. Identificar la refactorización a realizar: **Inline Variable**
2. En este caso se realiza Inline Variable de: `String mensaje = notificarExitoRegistro(nuevoObjetivo);`
3. La variable `mensaje` no se utiliza después de su asignación
4. Seleccionar la variable en IntelliJ y presionar `Ctrl+Alt+N` para hacer Inline
5. Realizar pruebas para verificar que no se haya modificado su comportamiento

**Código después de la refactorización**

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    String usuarioId = (String) session.getAttribute("usuarioId");
    
    String action = request.getParameter("action");
    
    if ("crear".equals(action)) {
        String nombre = request.getParameter("nombre");
        String categoria = request.getParameter("categoria");
        String frecuencia = request.getParameter("frecuencia");
        
        Objetivo nuevoObjetivo = crear(nombre, categoria, frecuencia);
        
        if (nuevoObjetivo != null) {
            notificarExitoRegistro(nuevoObjetivo);
            limpiarFormulario(request);
            response.sendRedirect("controlador-objetivos?action=listar&success=true");
        }
    }
}
```

En el código, la variable `mensaje` almacenaba el resultado de la notificación pero no se utilizaba posteriormente, lo cual era innecesario. Al aplicar Inline Variable, se llama directamente al método, lo que reduce la cantidad de código, mejora la legibilidad y elimina la variable adicional que no aportaba valor al flujo del método.

---

### 3. Extract Class en HabitoController.java

**Código antes de la refactorización**

```java
@WebServlet(name = "HabitoController", urlPatterns = {"/habitos", "/habito"})
public class HabitoController extends HttpServlet {
    
    private HabitoDAO habitoDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoDAO = new HabitoDAO();
    }
    
    private void listarHabitos(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        List<Habito> habitos = habitoDAO.findHabitosActivos(usuarioId);
        
        // Calcular estadísticas
        int habitosActivos = habitos.size();
        int habitosCompletadosHoy = 0;
        
        for (Habito habito : habitos) {
            if (habito.tieneMetaDelDia(LocalDate.now())) {
                habitosCompletadosHoy++;
            }
        }
        
        request.setAttribute("habitos", habitos);
        request.setAttribute("habitosActivos", habitosActivos);
        request.setAttribute("habitosCompletadosHoy", habitosCompletadosHoy);
        request.getRequestDispatcher("/WEB-INF/views/habitos/lista.jsp").forward(request, response);
    }
}
```

**Pasos de la refactorización**

1. Identificar que el controlador tiene lógica de negocio (cálculo de estadísticas)
2. Crear una nueva clase de servicio llamada `HabitoService` en el package `service`
3. Mover la lógica de negocio al servicio
4. El controlador delegará la lógica al servicio, manteniéndose enfocado en manejar las solicitudes HTTP
5. Usar la nueva clase de servicio en el Servlet

**Código después de la refactorización**

**Clase HabitoService.java:**
```java
package com.example.service;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import java.time.LocalDate;
import java.util.List;

public class HabitoService {
    
    private HabitoDAO habitoDAO;
    
    public HabitoService() {
        this.habitoDAO = new HabitoDAO();
    }
    
    public List<Habito> obtenerHabitosActivos(String usuarioId) {
        return habitoDAO.findHabitosActivos(usuarioId);
    }
    
    public int calcularHabitosCompletadosHoy(List<Habito> habitos) {
        int count = 0;
        for (Habito habito : habitos) {
            if (habito.tieneMetaDelDia(LocalDate.now())) {
                count++;
            }
        }
        return count;
    }
}
```

**Clase HabitoController.java refactorizada:**
```java
@WebServlet(name = "HabitoController", urlPatterns = {"/habitos", "/habito"})
public class HabitoController extends HttpServlet {
    
    private HabitoService habitoService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoService = new HabitoService();
    }
    
    private void listarHabitos(HttpServletRequest request, HttpServletResponse response, String usuarioId) 
            throws ServletException, IOException {
        
        List<Habito> habitos = habitoService.obtenerHabitosActivos(usuarioId);
        int habitosActivos = habitos.size();
        int habitosCompletadosHoy = habitoService.calcularHabitosCompletadosHoy(habitos);
        
        request.setAttribute("habitos", habitos);
        request.setAttribute("habitosActivos", habitosActivos);
        request.setAttribute("habitosCompletadosHoy", habitosCompletadosHoy);
        request.getRequestDispatcher("/WEB-INF/views/habitos/lista.jsp").forward(request, response);
    }
}
```

**HabitoService**: Encapsula la lógica de negocio para obtener hábitos y calcular estadísticas.  
**HabitoController**: Maneja la solicitud HTTP y delega la lógica de negocio al servicio.

Esta estructura mantiene las responsabilidades separadas según el patrón MVC y mejora la organización del código, facilitando su mantenimiento y ampliación en el futuro.

---

### 4. Introduce Explaining Variable en HabitoDAO.java

**Código antes de la refactorización**

```java
public List<Habito> findByUsuarioId(String usuarioId) {
    EntityManager em = EntityManagerUtil.getEntityManager();
    try {
        TypedQuery<Habito> query = em.createQuery(
            "SELECT h FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true ORDER BY h.fechaCreacion DESC", 
            Habito.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    } finally {
        em.close();
    }
}

public List<Habito> findHabitosActivos(String usuarioId) {
    EntityManager em = EntityManagerUtil.getEntityManager();
    try {
        TypedQuery<Habito> query = em.createQuery(
            "SELECT h FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true ORDER BY h.nombre", 
            Habito.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    } finally {
        em.close();
    }
}
```

**Pasos de la refactorización**

1. Identificar el refactoring a realizar: **Introduce Explaining Variable**
2. Se identifica que las consultas SQL se repiten con ligeras variaciones
3. Crear constantes para las consultas comunes
4. Reemplazar las consultas por las constantes
5. Llamar a las constantes en los métodos correctos

**Código después de la refactorización**

```java
public class HabitoDAO {
    
    // Constantes para consultas JPQL
    private static final String QUERY_HABITOS_ACTIVOS_BY_USER = 
        "SELECT h FROM Habito h WHERE h.usuarioId = :usuarioId AND h.activo = true";
    
    private static final String ORDER_BY_FECHA_DESC = " ORDER BY h.fechaCreacion DESC";
    private static final String ORDER_BY_NOMBRE = " ORDER BY h.nombre";
    
    public List<Habito> findByUsuarioId(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Habito> query = em.createQuery(
                QUERY_HABITOS_ACTIVOS_BY_USER + ORDER_BY_FECHA_DESC, 
                Habito.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Habito> findHabitosActivos(String usuarioId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Habito> query = em.createQuery(
                QUERY_HABITOS_ACTIVOS_BY_USER + ORDER_BY_NOMBRE, 
                Habito.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
```

Aplicando la técnica Introduce Explaining Variable a las consultas de la clase HabitoDAO, el código es más entendible y modificar las consultas resulta más sencillo ya que se cambia en una sola variable constante. Esto también reduce la duplicación y facilita el mantenimiento.

---

### 5. Try-with-Resources en HabitoDAO.java

**Código antes de la refactorización**

```java
public Habito save(Habito habito) {
    EntityManager em = EntityManagerUtil.getEntityManager();
    EntityTransaction tx = null;
    try {
        tx = em.getTransaction();
        tx.begin();
        
        if (habito.getId() == null) {
            em.persist(habito);
        } else {
            habito = em.merge(habito);
        }
        
        tx.commit();
        return habito;
    } catch (Exception e) {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        throw new RuntimeException("Error al guardar hábito", e);
    } finally {
        if (em != null) {
            em.close();
        }
    }
}
```

**Pasos de la refactorización**

1. El EntityManager se está cerrando manualmente en el bloque finally, lo cual puede ser simplificado usando try-with-resources
2. Esto asegura el cierre automático del EntityManager
3. Reemplazamos el bloque finally con try-with-resources
4. El rollback en el bloque catch se mantiene para transacciones activas
5. Con try-with-resources, no es necesario comprobar si el EntityManager es null
6. Realizar pruebas para verificar que no se haya modificado su comportamiento

**Código después de la refactorización**

```java
public Habito save(Habito habito) {
    try (EntityManager em = EntityManagerUtil.getEntityManager()) {
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
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
        }
    }
}
```

Este código es más simple, fácil de leer y menos propenso a errores debido al manejo automático del EntityManager. El uso de try-with-resources garantiza que el recurso se cierre correctamente incluso si ocurre una excepción, eliminando la necesidad del bloque finally.

---

### 6. Replace Magic Strings en ControladorHabitos.java

**Código antes de la refactorización**

```java
@WebServlet("/controlador-habitos")
public class ControladorHabitos extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("registrar".equals(action)) {
            String habitoIdStr = request.getParameter("habitoId");
            if (habitoIdStr != null) {
                Long habitoId = Long.parseLong(habitoIdStr);
                Habito habito = registrarCumplimiento(habitoId, LocalDate.now(), "COMPLETADO");
                
                if (habito != null) {
                    response.sendRedirect("habitos?action=list&success=true");
                } else {
                    response.sendRedirect("habitos?action=list&error=true");
                }
            }
        }
    }
}
```

**Pasos de la refactorización**

1. Identificar todas las cadenas "mágicas" (magic strings) en el código
2. Crear una clase de constantes en el package `utils`
3. Definir constantes para todas las acciones, parámetros y estados
4. Reemplazar las cadenas literales por las constantes
5. Verificar el funcionamiento

**Código después de la refactorización**

**Clase Constants.java:**
```java
package com.example.utils;

public class Constants {
    // Acciones
    public static final String ACTION_REGISTRAR = "registrar";
    public static final String ACTION_LIST = "list";
    
    // Parámetros
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_HABITO_ID = "habitoId";
    public static final String PARAM_SUCCESS = "success";
    public static final String PARAM_ERROR = "error";
    
    // Estados
    public static final String ESTADO_COMPLETADO = "COMPLETADO";
    
    private Constants() {
        throw new UnsupportedOperationException("Clase de utilidad");
    }
}
```

**ControladorHabitos.java refactorizado:**
```java
@WebServlet("/controlador-habitos")
public class ControladorHabitos extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter(Constants.PARAM_ACTION);
        
        if (Constants.ACTION_REGISTRAR.equals(action)) {
            String habitoIdStr = request.getParameter(Constants.PARAM_HABITO_ID);
            if (habitoIdStr != null) {
                Long habitoId = Long.parseLong(habitoIdStr);
                Habito habito = registrarCumplimiento(habitoId, LocalDate.now(), Constants.ESTADO_COMPLETADO);
                
                if (habito != null) {
                    response.sendRedirect("habitos?" + Constants.PARAM_ACTION + "=" + Constants.ACTION_LIST + 
                                        "&" + Constants.PARAM_SUCCESS + "=true");
                } else {
                    response.sendRedirect("habitos?" + Constants.PARAM_ACTION + "=" + Constants.ACTION_LIST + 
                                        "&" + Constants.PARAM_ERROR + "=true");
                }
            }
        }
    }
}
```

Esta refactorización elimina las "cadenas mágicas" del código, haciéndolo más mantenible. Si necesitamos cambiar el nombre de una acción o parámetro, solo debemos modificarlo en un lugar. Además, el código es más legible y menos propenso a errores tipográficos.

---

### 7. Extract Superclass - BaseController en package servlet

**Código antes de la refactorización**

```java
// En HabitoController.java
public class HabitoController extends HttpServlet {
    
    private String obtenerOCrearUsuarioId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null) {
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
            usuarioId = "demo";
        }
        return usuarioId;
    }
    
    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje) 
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}

// En ObjetivoController.java
public class ObjetivoController extends HttpServlet {
    
    // Mismo código duplicado...
    private String obtenerOCrearUsuarioId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null) {
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
            usuarioId = "demo";
        }
        return usuarioId;
    }
}
```

**Pasos de la refactorización**

1. Identificar código duplicado en múltiples controladores
2. Crear una clase base `BaseController` en el package `servlet`
3. Mover la funcionalidad común a la clase base
4. Hacer que todos los controladores hereden de `BaseController`
5. Eliminar el código duplicado de los controladores hijos

**Código después de la refactorización**

**Clase BaseController.java:**
```java
package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    
    protected String obtenerOCrearUsuarioId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String usuarioId = (String) session.getAttribute("usuarioId");
        
        if (usuarioId == null) {
            session.setAttribute("usuarioId", "demo");
            session.setAttribute("usuarioNombre", "Usuario Demo");
            usuarioId = "demo";
        }
        return usuarioId;
    }
    
    protected void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje) 
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
    
    protected void enviarRespuestaJSON(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
```

**HabitoController.java refactorizado:**
```java
@WebServlet(name = "HabitoController", urlPatterns = {"/habitos", "/habito"})
public class HabitoController extends BaseController {
    
    private HabitoService habitoService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        habitoService = new HabitoService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String usuarioId = obtenerOCrearUsuarioId(request); // Heredado de BaseController
        
        try {
            // Lógica del controlador...
        } catch (Exception e) {
            manejarError(request, response, "Error al procesar hábitos"); // Heredado
        }
    }
}
```

Esta refactorización elimina la duplicación de código entre controladores, centralizando la funcionalidad común en una clase base. Esto facilita el mantenimiento, ya que los cambios en la lógica común solo deben realizarse en un lugar, y asegura consistencia en el comportamiento de todos los controladores.

---

### 8. Simplify Conditional en ObjetivoController.java

**Código antes de la refactorización**

```java
public void actualizarEstado(Long objetivoId, String nuevoEstado) {
    Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
    
    if (objetivo != null) {
        try {
            Objetivo.EstadoObjetivo estado = Objetivo.EstadoObjetivo.valueOf(nuevoEstado.toUpperCase());
            objetivo.setEstado(estado);
            objetivoDAO.save(objetivo);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
```

**Pasos de la refactorización**

1. Identificar condicionales anidados que dificultan la lectura
2. Aplicar "Guard Clauses" (cláusulas de guarda) para manejar casos especiales primero
3. Reducir la indentación del código principal
4. Mejorar el manejo de errores

**Código después de la refactorización**

```java
public void actualizarEstado(Long objetivoId, String nuevoEstado) {
    Objetivo objetivo = objetivoDAO.findById(objetivoId).orElse(null);
    
    if (objetivo == null) {
        System.err.println("Objetivo no encontrado con ID: " + objetivoId);
        return;
    }
    
    try {
        Objetivo.EstadoObjetivo estado = Objetivo.EstadoObjetivo.valueOf(nuevoEstado.toUpperCase());
        objetivo.setEstado(estado);
        objetivoDAO.save(objetivo);
    } catch (IllegalArgumentException e) {
        System.err.println("Estado inválido: " + nuevoEstado);
        e.printStackTrace();
    }
}
```

Al aplicar Guard Clauses, se elimina la indentación innecesaria y se hace el código más legible. Los casos especiales se manejan primero con un return temprano, permitiendo que el flujo principal del método sea más claro y directo.

---

### 9. Remove Dead Code en UserServlet.java

**Código antes de la refactorización**

```java
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = new User(name, email);
        userDAO.save(user);

        resp.sendRedirect("users");
    }
}
```

**Pasos de la refactorización**

1. Identificar código obsoleto (Dead Code)
2. Este servlet ha sido reemplazado por `UsuarioServlet.java`
3. La clase `User` ha sido reemplazada por `Usuario`
4. Verificar que no haya referencias a este servlet en el código
5. Marcar como @Deprecated o eliminar el archivo

**Código después de la refactorización**

```java
/**
 * @deprecated Usar UsuarioServlet en su lugar
 * Esta clase se mantiene temporalmente por compatibilidad
 */
@Deprecated
@WebServlet("/users")
public class UserServlet extends HttpServlet {
    // Código marcado como obsoleto
    // Se eliminará en la próxima versión
}
```

O mejor aún, si no hay dependencias:

```java
// Archivo eliminado completamente
// Usar UsuarioServlet.java en su lugar
```

La eliminación de código muerto (Dead Code) reduce la complejidad del proyecto, elimina confusión sobre qué clases usar, y facilita el mantenimiento. Al marcar código como @Deprecated primero, damos tiempo para migrar cualquier uso restante antes de la eliminación completa.

---

### 10. Replace Nested Conditional with Guard Clauses en SeguimientoController.java

**Código antes de la refactorización**

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");
    
    if ("registrar".equals(action)) {
        String habitoIdStr = request.getParameter("habitoId");
        if (habitoIdStr != null) {
            try {
                Long habitoId = Long.parseLong(habitoIdStr);
                String completadoStr = request.getParameter("completado");
                if (completadoStr != null) {
                    int completado = Integer.parseInt(completadoStr);
                    String observaciones = request.getParameter("observaciones");
                    
                    boolean exito = registrarSeguimiento(habitoId, completado, observaciones);
                    if (exito) {
                        response.sendRedirect("seguimiento?success=true");
                    } else {
                        response.sendRedirect("seguimiento?error=true");
                    }
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("seguimiento?error=formato");
            }
        }
    }
}
```

**Pasos de la refactorización**

1. Identificar condicionales anidados múltiples (más de 3 niveles)
2. Aplicar Guard Clauses para validaciones tempranas
3. Reducir la profundidad de indentación
4. Mejorar la legibilidad del flujo principal

**Código después de la refactorización**

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");
    
    if (!"registrar".equals(action)) {
        response.sendRedirect("seguimiento");
        return;
    }
    
    String habitoIdStr = request.getParameter("habitoId");
    if (habitoIdStr == null) {
        response.sendRedirect("seguimiento?error=parametros");
        return;
    }
    
    String completadoStr = request.getParameter("completado");
    if (completadoStr == null) {
        response.sendRedirect("seguimiento?error=parametros");
        return;
    }
    
    try {
        Long habitoId = Long.parseLong(habitoIdStr);
        int completado = Integer.parseInt(completadoStr);
        String observaciones = request.getParameter("observaciones");
        
        boolean exito = registrarSeguimiento(habitoId, completado, observaciones);
        
        String redirect = exito ? "seguimiento?success=true" : "seguimiento?error=true";
        response.sendRedirect(redirect);
        
    } catch (NumberFormatException e) {
        response.sendRedirect("seguimiento?error=formato");
    }
}
```

Al reemplazar los condicionales anidados con Guard Clauses, el código se vuelve más plano y legible. Cada validación se maneja inmediatamente con un return temprano, permitiendo que el flujo principal del método sea claro y directo, sin múltiples niveles de indentación.

---

## Conclusiones

Las refactorizaciones aplicadas al Sistema de Seguimiento de Hábitos han mejorado significativamente la calidad del código sin alterar su funcionalidad. Los principales beneficios obtenidos son:

1. **Mejora en la legibilidad**: El código es más fácil de entender para desarrolladores nuevos y existentes.

2. **Reducción de duplicación**: La extracción de métodos comunes a clases base elimina código repetido.

3. **Mejor separación de responsabilidades**: La creación de servicios separa la lógica de negocio de los controladores.

4. **Código más mantenible**: Las constantes y variables explicativas facilitan los cambios futuros.

5. **Gestión de recursos mejorada**: El uso de try-with-resources asegura el cierre correcto de recursos.

6. **Menor complejidad**: Las Guard Clauses reducen la profundidad de anidamiento.

Estas técnicas de refactorización son fundamentales en Extreme Programming y deben aplicarse continuamente para mantener un código limpio y sostenible. El principio clave es realizar cambios pequeños e incrementales, siempre validando que el comportamiento del sistema permanezca intacto mediante pruebas.

---

**Referencias:**
- Fowler, M. (2018). Refactoring: Improving the Design of Existing Code (2nd Edition)
- Beck, K. (2004). Extreme Programming Explained: Embrace Change
- IntelliJ IDEA Refactoring Documentation
