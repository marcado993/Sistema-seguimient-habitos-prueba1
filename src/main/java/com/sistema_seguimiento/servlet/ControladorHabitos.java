package com.sistema_seguimiento.servlet;

import com.sistema_seguimiento.dao.HabitoDAO;
import com.sistema_seguimiento.dao.PetDAO;
import com.sistema_seguimiento.model.*;
import com.sistema_seguimiento.services.HabitoServicio;
import com.sistema_seguimiento.services.PetUnlockService;
import com.sistema_seguimiento.services.PointsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de Hábitos según el diagrama de clases
 * Maneja las operaciones relacionadas con hábitos
 * 
 * 🟢 FASE VERDE - Integrado con PointsService
 */
@WebServlet("/controlador-habitos")
public class ControladorHabitos extends HttpServlet {

    private final HabitoServicio habitoServicio = new HabitoServicio();
    
    /**
     * 🟢 FASE VERDE - PointsService para gamificación
     */
    private PointsService pointsService = new PointsService();
    private RegistroHabito registroExistente;

    @Override
    public void init() throws ServletException {
        super.init();
        habitoServicio.setHabitoDAO(new HabitoDAO());
    }
    
    /**
     * 🟢 FASE VERDE - Setter para inyección de dependencias (usado en tests)
     */
    public void setHabitoDAO(HabitoDAO habitoDAO) {
        habitoServicio.setHabitoDAO(habitoDAO);
    }
    
    /**
     * 🟢 FASE VERDE - Setter para inyección de PointsService (usado en tests)
     */
    public void setPointsService(PointsService pointsService) {
        this.pointsService = pointsService;
    }
    
    /**
     * Obtener el usuarioId de la sesión
     */
    private Integer getUsuarioIdFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return (usuario != null) ? usuario.getId() : null;
    }
    
    /**
     * Registrar cumplimiento de un hábito
     * Basado en el diagrama de secuencia
     */
    public Habito registrarCumplimiento(Integer habitoId, LocalDate fecha, String observacion) {
        return habitoServicio.registrarCumplimiento(habitoId, fecha, observacion);
    }
    
    /**
     * Obtener registros de un hábito en un rango de fechas
     */
    public List<RegistroHabito> obtenerRegistros(Integer habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        return habitoServicio.obtenerRegistros(habitoId, fechaInicio, fechaFin);
    }
    
    /**
     * Obtener todos los registros de hoy del usuario
     */
    public List<RegistroHabito> obtenerRegistrosDeHoy(Integer usuarioId) {
        return habitoServicio.obtenerRegistrosDeHoy(usuarioId);
    }
    
    /**
     * Buscar hábito por ID
     */
    public Habito buscarHabito(Integer habitoId) {
        return habitoServicio.buscarHabito(habitoId);
    }
    
    /**
     * Crear ficha de racha actual
     */
    public int crearFichaRacha(Habito habito, String estado, boolean cumplido) {
        return habitoServicio.crearFichaRacha(habito, estado, cumplido);
    }
    
    /**
     * Crear nuevo registro de hábito
     */
    public RegistroHabito crearNuevoRegistro(Habito habito, LocalDate fecha, String observacion) {
        return habitoServicio.crearNuevoRegistro(habito, fecha, observacion);
    }
    
    /**
     * Obtener lista de hábitos del usuario
     */
    public List<Habito> listarHabitosUsuario(Integer usuarioId) {
        return habitoServicio.listarHabitosUsuario(usuarioId);
    }
    
    /**
     * Guardar o actualizar hábito
     */
    public Habito guardarHabito(Habito habito) {
        return habitoServicio.guardarHabito(habito);
    }
    
    /**
     * Eliminar hábito (soft delete)
     */
    public boolean eliminarHabito(Integer habitoId) {
        return habitoServicio.eliminarHabito(habitoId);
    }
    
    /**
     * Obtener estadísticas del usuario
     */
    public Long obtenerHabitosCompletadosHoy(Integer usuarioId) {
        return habitoServicio.obtenerHabitosCompletadosHoy(usuarioId);
    }
    
    /**
     * Obtener porcentaje de completado de la semana
     */
    public Double obtenerPorcentajeSemana(Integer usuarioId) {
        return habitoServicio.obtenerPorcentajeSemana(usuarioId);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // Obtener usuarioId de la sesión
        HttpSession session = request.getSession(false);
        Integer usuarioId = getUsuarioIdFromSession(session);
        
        if (usuarioId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            procesarAccion(request, response, action, usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("controlador-habitos?action=list&error=exception");
        }
    }

    /**
     * Procesa la acción solicitada por el usuario para las peticiones GET.
     * Actúa como un despachador que, basándose en el parámetro ´action´,
     * delega la ejecución a la lógica correpondiente para listar, ver o eliminar hábitos.
     * @param request El objeto HttpServletRequest que contiene la solicitud del cliente
     * @param response El objeto HttpServletResponse para envíar la respuesta.
     * @param action La acción específica a realizar (ej. "list", "view", "delete").
     * @param usuarioId El ID del usuario para filtrar los datos correspondientes.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    private void procesarAccion(HttpServletRequest request, HttpServletResponse response, String action, Integer usuarioId) throws ServletException, IOException {
        switch (action) {
            case "list":
                listarHabitos(request, response, usuarioId);
                break;
            case "registrar":
                mostrarFormularioRegistro(request, response, usuarioId);
                break;
            case "view":
                verSeguimiento(request, response, usuarioId);
                break;
            case "delete":
            case "eliminar":
                eliminarHabito(request, response, usuarioId);
                break;
            case "editar":
                editarHabito(request, response, usuarioId);
                break;
            default:// Por defecto, listar hábitos
                response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId);
                break;
        }
    }
    
    /**
     * Cargar hábito para editar
     */
    private void editarHabito(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws ServletException, IOException {
        String habitoIdStr = request.getParameter("habitoId");
        if (habitoIdStr != null) {
            Integer habitoId = Integer.parseInt(habitoIdStr);
            Habito habito = habitoServicio.buscarHabito(habitoId);
            
            if (habito != null && habito.getUsuarioId().equals(usuarioId)) {
                request.setAttribute("habito", habito);
                request.setAttribute("modoEdicion", true);
                request.getRequestDispatcher("/WEB-INF/views/registroHabito.jsp").forward(request, response);
            } else {
                response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&error=notfound");
            }
        } else {
            response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId);
        }
    }

    /**
     * Elimina un hábito específico del usuario.
     * Gestionar la petición para eliminar un hábito
     * Obtieen el ID del hábito desde los parámetros de la solicitud,
     * invoca a la lógica de negocio para su eliminación y redirige al usuario
     * a la lista de hábitos con un mensaje de éxito o error.
     * @param request El objeto HttpServletRequest que contiene la solicitud.
     * @param response El objetp HttpServletResponse para envíar la respuesta de redirección.
     * @param usuarioId El identificador del usuario propietario del hábito.
     * @throws IOException si ocurre un error durante la redirección.
     */
    private void eliminarHabito(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws IOException {
        String habitoIdStr = request.getParameter("habitoId");
        if (habitoIdStr != null) {
            Integer habitoId = Integer.parseInt(habitoIdStr);
            boolean success = habitoServicio.eliminarHabito(habitoId);

            if (success) {
                response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&deleted=true");
            } else {
                response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=delete");
            }
        }
    }

    /**
     * Muestra la vista de seguimiento de hábitos.
     * Si se proporciona un ID de hábito, muestra la vista detallada de ese hábito específico
     * incluyendo su historial de registros. Si no, muestra la vista general de todos los
     * hábitos del usuario.
     * @param request El objeto HttpServletRequests, donde se almacenarán los datos para la vista.
     * @param response El objeto HttpServletResponse para hacer el forward a la vista JSP.
     * @param usuarioId El identificador del usuario cuyos hábitos se van a mostrar.
     * @throws ServletException Por si ocurre un error durante el forward.
     * @throws IOException Por si ocurre un error de entrada/salida.
     */
    private void verSeguimiento(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws ServletException, IOException {
        String habitoIdStr = request.getParameter("habitoId");

        if (habitoIdStr != null && !habitoIdStr.isEmpty()) {
            // Ver detalle de un hábito específico
            Integer habitoId = Integer.parseInt(habitoIdStr);
            Habito habito = habitoServicio.buscarHabito(habitoId);

            if (habito != null) {
                LocalDate hoy = LocalDate.now();
                LocalDate hace30Dias = hoy.minusDays(30);
                List<RegistroHabito> registros = habitoServicio.obtenerRegistros(habitoId, hace30Dias, hoy);

                request.setAttribute("habito", habito);
                request.setAttribute("registros", registros);
                request.setAttribute("racha", habito.calcularRachaActual());
                request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);

            } else {
                response.sendRedirect("controlador-habitos?action=list&error=notfound");
            }
        } else {
            // Ver todos los hábitos del usuario (vista de seguimiento general)
            System.out.println("📊 Cargando vista de seguimiento para usuario: " + usuarioId);
            List<Habito> habitos = habitoServicio.listarHabitosUsuario(usuarioId);
            System.out.println("📋 Hábitos encontrados: " + habitos.size());
            request.setAttribute("habitos", habitos);
            request.getRequestDispatcher("/WEB-INF/views/vistaSeguimiento.jsp").forward(request, response);
        }
    }

    /**
     * Procesa el formulario de registro de hábito (POST)
     */
    private void procesarRegistroHabito(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws IOException {
        String habitoIdStr = request.getParameter("habitoId");
        String fechaStr = request.getParameter("fecha");
        String estadoStr = request.getParameter("estado");
        String notas = request.getParameter("notas");  // ✅ CORREGIDO: Usar "notas" en vez de "observacion"
        
        System.out.println("📝 Procesando registro de hábito:");
        System.out.println("   - Hábito ID: " + habitoIdStr);
        System.out.println("   - Fecha: " + fechaStr);
        System.out.println("   - Estado: " + estadoStr);
        
        if (habitoIdStr != null && !habitoIdStr.isEmpty()) {
            try {
                Integer habitoId = Integer.parseInt(habitoIdStr);
                LocalDate fecha = fechaStr != null ? LocalDate.parse(fechaStr) : LocalDate.now();
                
                Habito habito = habitoServicio.registrarCumplimiento(habitoId, fecha, notas);  // ✅ CORREGIDO
                
                if (habito != null) {
                    System.out.println("✅ Registro exitoso del hábito ID: " + habitoId);
                    
                    // 🟢 FASE VERDE - Agregar puntos al usuario después del registro exitoso
                    if (estadoStr != null && pointsService != null) {
                        pointsService.addPointsToUser(usuarioId, estadoStr);
                        System.out.println("🎮 Puntos procesados para estado: " + estadoStr);
                    }
                    
                    response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&success=true");
                } else {
                    System.out.println("❌ Error al registrar hábito");
                    response.sendRedirect("controlador-habitos?action=registrar&usuarioId=" + usuarioId + "&error=save");
                }
            } catch (Exception e) {
                System.err.println("❌ Error al procesar registro: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect("controlador-habitos?action=registrar&usuarioId=" + usuarioId + "&error=exception");
            }
        } else {
            System.out.println("❌ No se proporcionó habitoId");
            response.sendRedirect("controlador-habitos?action=registrar&usuarioId=" + usuarioId + "&error=missing");
        }
    }    /**
     * Obtiene y muestra la lista de todos los hábitos activos de un usuario.
     * Prepara los datos necesarios y los reenvía a la vista JSP encargada de
     * renderizar la lista de hábitos para sus registros.
     * @param request El objeto HttpServletRequest, donde se guardará la lista de hábitos.
     * @param response El objeto HttpServletResponse para hacer el forward a la vista.
     * @param usuarioId El Identificador del usuario cuyos hábitos se listarán.
     * @throws ServletException si ocurre un error durante el forward.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    private void listarHabitos(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws ServletException, IOException {
        List<Habito> habitos = habitoServicio.listarHabitosUsuario(usuarioId);
        request.setAttribute("habitos", habitos);
        request.getRequestDispatcher("/WEB-INF/views/registroHabito.jsp").forward(request, response);
    }


    private Integer obtenerMetaDiariaOPredeterminada(Habito habito) {
        return habito.getMetaDiaria() != null ? habito.getMetaDiaria() : 1;
    }

/**
     * Procesa la solicitud para registrar el cumplimiento de un hábito con estado específico.
     * Obtiene los detalles del registro desde los parámetros de la solicitud, crea el registro
     * con el estado correspondiente (CUMPLIDO, NO_CUMPLIDO, PARCIAL) y redirige al usuario a la
     * lista de hábitos con un mensaje de éxito o error.
     * @param request El objeto HttpServletRequest que contiene los detalles del registro.
     * @param response El objeto HttpServletResponse para la redirección.
     * @param usuarioId El ID del usuario que realiza el registro.
     * @throws IOException si ocurre un error durante la redirección.
     */
private void procesarRegistroCumplimiento(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws IOException {
    String habitoIdStr = request.getParameter("habitoId");
    String notas = request.getParameter("notas");
    String vecesRealizadoStr = request.getParameter("vecesRealizado");
    String estadoAnimo = request.getParameter("estadoAnimo");
    String fechaStr = request.getParameter("fecha");
    String estado = request.getParameter("estado"); // CUMPLIDO, NO_CUMPLIDO, PARCIAL

    System.out.println("--- Iniciando procesarRegistroCumplimiento ---");
    System.out.println("   Recibido habitoId: " + habitoIdStr);
    System.out.println("   Recibido estado: " + estado); // <-- ¡VERIFICA ESTO EN EL LOG!
    System.out.println("   Recibido fechaStr: " + fechaStr); // <-- ¡VERIFICA ESTO EN EL LOG!
    System.out.println("   Recibido vecesRealizadoStr: " + vecesRealizadoStr);
    System.out.println("   Recibido estadoAnimo: " + estadoAnimo);
    System.out.println("   Recibido notas: " + notas);


    // 1. Validar y Parsear Parámetros Esenciales
    Integer habitoId = null;
    LocalDate fecha = LocalDate.now(); // Default a hoy

    if (habitoIdStr == null || habitoIdStr.trim().isEmpty()) {
        System.err.println("❌ Error: No se proporcionó habitoId.");
        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=missing_habitoId");
        return;
    }
    try {
        habitoId = Integer.parseInt(habitoIdStr);
    } catch (NumberFormatException e) {
        System.err.println("❌ Error: habitoId inválido: " + habitoIdStr);
        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=invalid_habitoId");
        return;
    }

    if (fechaStr != null && !fechaStr.trim().isEmpty()) {
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            System.err.println("❌ Error al parsear fecha: " + fechaStr + " - Usando fecha actual.");
            // Podrías redirigir con error si la fecha es obligatoria y mal formateada
            // response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=invalid_date");
            // return;
        }
    } else {
        System.out.println("   -> Fecha no proporcionada, usando fecha actual: " + fecha);
    }

    // Si estado sigue siendo null aquí, el problema está 100% en el JSP
    if (estado == null || estado.trim().isEmpty()) {
        System.err.println("⚠️ Advertencia: El parámetro 'estado' (CUMPLIDO/PARCIAL/NO_CUMPLIDO) no llegó del formulario.");
        // Puedes decidir si continuar, poner un default, o fallar
        // Por ahora, continuaremos, pero la lógica de puntos fallará.
        // estado = "NO_CUMPLIDO"; // Ejemplo de default si quisieras
    }


    // 2. Buscar Hábito
    Habito habito = habitoServicio.buscarHabito(habitoId);
    if (habito == null) {
        System.err.println("❌ Error: Hábito no encontrado con ID: " + habitoId);
        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=habito_notfound");
        return;
    }

    // 3. Parsear 'vecesRealizado'
    Integer vecesRealizado = 0; // Default a 0 si no se cumple nada
    if (vecesRealizadoStr != null && !vecesRealizadoStr.isEmpty()) {
        try {
            vecesRealizado = Integer.parseInt(vecesRealizadoStr);
            if (vecesRealizado < 0) vecesRealizado = 0; // No permitir negativos
        } catch (NumberFormatException e) {
            System.err.println("   -> vecesRealizado inválido: " + vecesRealizadoStr + " - Usando default 0.");
            // Si el estado es CUMPLIDO o PARCIAL, quizá poner 1 por defecto? Depende tu lógica.
            if ("CUMPLIDO".equals(estado) || "PARCIAL".equals(estado)) {
                vecesRealizado = 1; // Un default razonable si el formato falla pero se marcó como hecho
            } else {
                vecesRealizado = 0;
            }
        }
    } else if ("CUMPLIDO".equals(estado)) {
        // Si no se envió 'vecesRealizado' pero se marcó CUMPLIDO, asumir meta cumplida
        vecesRealizado = obtenerMetaDiariaOPredeterminada(habito);
        System.out.println("   -> vecesRealizado no proporcionado pero estado=CUMPLIDO, asumiendo meta: " + vecesRealizado);
    } else if ("PARCIAL".equals(estado)) {
        // Si no se envió 'vecesRealizado' pero se marcó PARCIAL, asumir 1
        vecesRealizado = 1;
        System.out.println("   -> vecesRealizado no proporcionado pero estado=PARCIAL, asumiendo 1.");
    } else {
        System.out.println("   -> vecesRealizado no proporcionado y estado no es CUMPLIDO/PARCIAL, usando default 0.");
        vecesRealizado = 0;
    }


    // 4. Buscar o Crear/Actualizar RegistroHabito
    RegistroHabito registroParaGuardar = null;
    RegistroHabito registroGuardado = null;
    HabitoDAO dao = habitoServicio.getHabitoDAO();

    try {
        // Buscar si ya existe un registro para este hábito en esta fecha
        registroExistente = dao.findRegistroByFecha(habitoId, fecha);

        Integer metaDiaria = obtenerMetaDiariaOPredeterminada(habito);
        // Determinar el estado 'completado' basado en la lógica
        boolean cumplido;
        if ("NO_CUMPLIDO".equals(estado)) {
            cumplido = false;
            // Si no se cumplió, forzar vecesRealizado a 0 podría ser una opción
            // vecesRealizado = 0;
        } else {
            // Si es CUMPLIDO, PARCIAL, o estado es null, basarse en vecesRealizado vs meta
            cumplido = (vecesRealizado >= metaDiaria);
        }

        if (registroExistente != null) {
            // Actualizar el existente
            System.out.println("   -> Registro existente encontrado (ID: " + registroExistente.getId() + "), actualizando...");
            registroParaGuardar = registroExistente;
            registroParaGuardar.setNotas(notas); // Actualizar campos
            registroParaGuardar.setVecesRealizado(vecesRealizado);
            registroParaGuardar.setEstadoAnimo(estadoAnimo != null && !estadoAnimo.isEmpty() ? estadoAnimo : registroParaGuardar.getEstadoAnimo()); // Mantener el ánimo si no se envía nuevo
            registroParaGuardar.setCompletado(cumplido); // Actualizar estado de completado

        } else {
            // Crear uno nuevo
            System.out.println("   -> No existe registro para esta fecha, creando uno nuevo...");
            registroParaGuardar = new RegistroHabito();
            registroParaGuardar.setHabito(habito);
            registroParaGuardar.setFecha(fecha);
            registroParaGuardar.setNotas(notas);
            registroParaGuardar.setVecesRealizado(vecesRealizado);
            registroParaGuardar.setEstadoAnimo(estadoAnimo != null && !estadoAnimo.isEmpty() ? estadoAnimo : "neutral");
            registroParaGuardar.setCompletado(cumplido);
        }

        System.out.println("📝 Objeto RegistroHabito listo para guardar/actualizar:");
        System.out.println("   - ID (si existe): " + registroParaGuardar.getId());
        System.out.println("   - Hábito ID: " + habitoId);
        System.out.println("   - Fecha: " + fecha);
        System.out.println("   - Veces realizado: " + registroParaGuardar.getVecesRealizado());
        System.out.println("   - Calculado Completado: " + registroParaGuardar.getCompletado());
        System.out.println("   - Estado de ánimo: " + registroParaGuardar.getEstadoAnimo());
        System.out.println("   - Notas: " + registroParaGuardar.getNotas());

        // 5. Guardar (Insertar o Actualizar) el Registro
        registroGuardado = dao.saveRegistro(registroParaGuardar); // saveRegistro debe manejar merge si el ID ya existe

    } catch (Exception e) {
        System.err.println("❌ Error CRÍTICO durante búsqueda o preparación/guardado de RegistroHabito: " + e.getMessage());
        e.printStackTrace();
        // Redirigir a una página de error o a la lista con un mensaje de error DB
        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=dberror_register");
        return; // Salir del método
    }


    // 6. Ejecutar Lógica Post-Guardado (Puntos y Mascotas) SOLO si se guardó bien
    if (registroGuardado != null && registroGuardado.getId() != null) {
        System.out.println("✅ Registro de hábito guardado/actualizado ID: " + registroGuardado.getId());

        // Llamada a PointsService (Usa el 'estado' recibido del form)
        if (estado != null && pointsService != null) {
            pointsService.addPointsToUser(usuarioId, estado);
            // El log está dentro de addPointsToUser
        } else {
            System.out.println("   -> No se procesaron puntos (estado nulo o pointsService nulo)");
        }

        // --- Lógica de PetUnlockService (Tarea 8) ---
        try {
            // Usar el mismo DAO instanciado antes
            long totalHabitosCumplidos = dao.countTotalHabitosCumplidosPorUsuario(usuarioId);
            System.out.println("🐾 Total hábitos/días cumplidos para PetUnlockService: " + totalHabitosCumplidos);

            PetUnlockService petUnlockService = new PetUnlockService();
            PetType tipoMascotaAlcanzado = petUnlockService.checkEvolution(Long.valueOf(usuarioId), (int) totalHabitosCumplidos);

            if (tipoMascotaAlcanzado != null) {
                System.out.println("🐾 Umbral de mascota alcanzado según PetUnlockService: " + tipoMascotaAlcanzado);
                PetDAO petDAO = new PetDAO();
                Optional<UserPet> currentPetOpt = petDAO.getCurrentUserPet(Long.valueOf(usuarioId));

                if (currentPetOpt.isPresent()) {
                    UserPet currentPet = currentPetOpt.get();
                    if (currentPet.getState().ordinal() < tipoMascotaAlcanzado.ordinal()) {
                        System.out.println("    -> Actualizando estado de mascota existente de " + currentPet.getState() + " a: " + tipoMascotaAlcanzado);
                        petDAO.updatePetState(Long.valueOf(usuarioId), tipoMascotaAlcanzado);
                    } else {
                        System.out.println("    -> Mascota actual (" + currentPet.getState() + ") ya es de este tipo o superior.");
                    }
                } else {
                    if (tipoMascotaAlcanzado == PetType.HUEVO) {
                        System.out.println("    -> Creando nueva mascota (HUEVO) para usuario " + usuarioId);
                        petDAO.saveNewPet(Long.valueOf(usuarioId), tipoMascotaAlcanzado);
                    } else {
                        System.out.println("    -> Se alcanzó umbral " + tipoMascotaAlcanzado + " pero no hay mascota base (HUEVO) aún.");
                    }
                }
            } else {
                System.out.println("🐾 No se alcanzó un nuevo umbral de mascota según PetUnlockService.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al ejecutar la lógica de PetUnlockService/PetDAO: " + e.getMessage());
            e.printStackTrace();
            // Considera loggear este error, pero continuar con la redirección de éxito
        }
        // --- FIN TAREA 8 ---

        // Redirección de éxito final
        response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&success=true");
        return; // Salir del método

    } else { // Falló al guardar/actualizar el registro
        System.err.println("❌ Error: registroGuardado fue null o no tiene ID después de intentar guardar/actualizar.");
        response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=register_save_failed");
        return;
    }
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // Obtener usuarioId de la sesión
        HttpSession session = request.getSession(false);
        Integer usuarioId = getUsuarioIdFromSession(session);
        
        if (usuarioId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            if ("registrar".equals(action)) {
                // Procesar registro de hábito cumplido
                procesarRegistroCumplimiento(request, response, usuarioId);
                
            } else if ("crear-con-objetivo".equals(action)) {
                // Crear hábito asociado a un objetivo
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String frecuenciaStr = request.getParameter("frecuencia");
                String metaDiariaStr = request.getParameter("metaDiaria");
                String objetivoIdStr = request.getParameter("objetivoId");
                String fechaInicioStr = request.getParameter("fechaInicio");
                
                System.out.println("🎯 Creando hábito con objetivo:");
                System.out.println("   - Nombre: " + nombre);
                System.out.println("   - Descripción: " + descripcion);
                System.out.println("   - Frecuencia: " + frecuenciaStr);
                System.out.println("   - Meta Diaria: " + metaDiariaStr);
                System.out.println("   - Fecha Inicio: " + fechaInicioStr);
                System.out.println("   - Usuario ID: " + usuarioId);
                
                try {
                    Habito habito = new Habito();
                    habito.setNombre(nombre);
                    habito.setDescripcion(descripcion);
                    habito.setUsuarioId(usuarioId);
                    habito.setActivo(true);
                    
                    // ✅ Estado de ánimo con valor por defecto (no se pide en el formulario)
                    habito.setEstadoAnimo("neutral");
                    
                    if (frecuenciaStr != null && !frecuenciaStr.isEmpty()) {
                        habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr.toUpperCase()));
                    } else {
                        habito.setFrecuencia(Habito.FrecuenciaHabito.DIARIA); // Default
                    }
                    
                    if (metaDiariaStr != null && !metaDiariaStr.isEmpty()) {
                        habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
                    } else {
                        habito.setMetaDiaria(1); // Default
                    }
                    
                    if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                        habito.setFechaInicio(LocalDate.parse(fechaInicioStr));
                    } else {
                        habito.setFechaInicio(LocalDate.now()); // Default
                    }
                    
                    Habito habitoGuardado = habitoServicio.guardarHabito(habito);
                    
                    if (habitoGuardado != null && habitoGuardado.getId() != null) {
                        System.out.println("✅ Hábito guardado exitosamente con ID: " + habitoGuardado.getId());
                        request.getSession().setAttribute("mensaje", "✅ Hábito '" + nombre + "' creado exitosamente");
                        response.sendRedirect(request.getContextPath() + "/controlador-objetivos?action=listar");
                    } else {
                        System.err.println("❌ Error: habitoGuardado es null o no tiene ID");
                        request.getSession().setAttribute("error", "❌ Error al guardar el hábito. Intenta nuevamente.");
                        response.sendRedirect(request.getContextPath() + "/planificar");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Excepción al crear hábito: " + e.getMessage());
                    e.printStackTrace();
                    request.getSession().setAttribute("error", "❌ Error: " + e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/planificar");
                }
                
            } else if ("actualizar".equals(action)) {
                // Actualizar hábito existente
                String habitoIdStr = request.getParameter("habitoId");
                if (habitoIdStr != null) {
                    Integer habitoId = Integer.parseInt(habitoIdStr);
                    Habito habito = habitoServicio.buscarHabito(habitoId);
                    
                    if (habito != null && habito.getUsuarioId().equals(usuarioId)) {
                        // Actualizar campos
                        habito.setNombre(request.getParameter("nombre"));
                        habito.setDescripcion(request.getParameter("descripcion"));
                        
                        String frecuenciaStr = request.getParameter("frecuencia");
                        String metaDiariaStr = request.getParameter("metaDiaria");
                        String fechaInicioStr = request.getParameter("fechaInicio");
                        
                        if (frecuenciaStr != null && !frecuenciaStr.isEmpty()) {
                            habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr));
                        }
                        
                        if (metaDiariaStr != null && !metaDiariaStr.isEmpty()) {
                            habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
                        }
                        
                        if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                            habito.setFechaInicio(LocalDate.parse(fechaInicioStr));
                        }
                        
                        Habito habitoGuardado = habitoServicio.guardarHabito(habito);
                        
                        if (habitoGuardado != null) {
                            request.getSession().setAttribute("mensaje", "✅ Hábito actualizado exitosamente");
                            response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId);
                        } else {
                            response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&error=save");
                        }
                    } else {
                        response.sendRedirect("controlador-habitos?action=view&usuarioId=" + usuarioId + "&error=permission");
                    }
                }
                
            } else if ("create".equals(action) || "update".equals(action)) {
                // Crear o actualizar hábito
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String frecuenciaStr = request.getParameter("frecuencia");
                String metaDiariaStr = request.getParameter("metaDiaria");
                String habitoIdStr = request.getParameter("habitoId");
                
                Habito habito;
                if (habitoIdStr != null && !habitoIdStr.isEmpty()) {
                    // Actualizar hábito existente
                    Integer habitoId = Integer.parseInt(habitoIdStr);
                    habito = habitoServicio.buscarHabito(habitoId);
                    if (habito == null) {
                        response.sendRedirect("controlador-habitos?action=list&error=notfound");
                        return;
                    }
                } else {
                    // Crear nuevo hábito
                    habito = new Habito();
                    habito.setUsuarioId(usuarioId);
                }
                
                // Actualizar datos
                habito.setNombre(nombre);
                habito.setDescripcion(descripcion);
                
                if (frecuenciaStr != null && !frecuenciaStr.isEmpty()) {
                    habito.setFrecuencia(Habito.FrecuenciaHabito.valueOf(frecuenciaStr));
                }
                
                if (metaDiariaStr != null && !metaDiariaStr.isEmpty()) {
                    habito.setMetaDiaria(Integer.parseInt(metaDiariaStr));
                }
                
                Habito habitoGuardado = habitoServicio.guardarHabito(habito);
                
                if (habitoGuardado != null) {
                    response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&saved=true");
                } else {
                    response.sendRedirect("controlador-habitos?action=list&usuarioId=" + usuarioId + "&error=save");
                }
                
            } else if ("registrar".equals(action)) {
                procesarRegistroCumplimiento(request, response, usuarioId);
                
            } else {
                // Acción no reconocida, redirigir a GET
                doGet(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("controlador-habitos?action=list&error=exception");
        }
    }
    
    /**
     * Mostrar formulario para registrar hábito diario
     */
    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response, Integer usuarioId) throws ServletException, IOException {
        System.out.println("📝 Mostrando formulario de registro de hábitos para usuario: " + usuarioId);
        
        // Obtener todos los hábitos del usuario para que pueda seleccionar cuál registrar
        List<Habito> habitos = habitoServicio.listarHabitosUsuario(usuarioId);
        System.out.println("📋 Hábitos encontrados para registro: " + habitos.size());
        
        request.setAttribute("habitos", habitos);
        request.getRequestDispatcher("/WEB-INF/views/registroHabito.jsp").forward(request, response);
    }
}
