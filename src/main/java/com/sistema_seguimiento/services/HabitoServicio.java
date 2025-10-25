package com.sistema_seguimiento.services;

import com.sistema_seguimiento.dao.HabitoDAO;
import com.sistema_seguimiento.model.Habito;
import com.sistema_seguimiento.model.RegistroHabito;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitoServicio implements Serializable {
    public HabitoDAO habitoDAO;

    public HabitoDAO getHabitoDAO() {
        return habitoDAO;
    }

    public void setHabitoDAO(HabitoDAO habitoDAO) {
        this.habitoDAO = habitoDAO;
    }

    public HabitoServicio() {
    }

    /**
     * Registrar cumplimiento de un hábito
     * Basado en el diagrama de secuencia
     */
    public Habito registrarCumplimiento(Integer habitoId, LocalDate fecha, String observacion) {
        try {
            // 1) buscarHabito(habitoId)
            Habito habito = buscarHabito(habitoId);
            if (habito == null) {
                System.err.println("[ControladorHabitos] registrarCumplimiento: hábito no encontrado id=" + habitoId);
                return null;
            }

            // 2) crear(fechaActual, estado='CUMPLIDO') -> representado creando un RegistroHabito
            System.out.println("[ControladorHabitos] Crear nuevo registro para habitoId=" + habitoId + " fecha=" + fecha);
            RegistroHabito nuevoRegistro = crearNuevoRegistro(habito, fecha, observacion);

            // 3) si el registro fue guardado correctamente, se considera agregado al historial
            if (nuevoRegistro != null && nuevoRegistro.getId() != null) {
                System.out.println("[ControladorHabitos] Nuevo registro creado id=" + nuevoRegistro.getId());
            } else {
                // Fallback: intentar marcar como completado directamente en DAO si la creación de registro falla
                System.out.println("[ControladorHabitos] No se pudo crear registro, llamando a registrarCompletado DAO como fallback");
                habitoDAO.registrarCompletado(habitoId, fecha, observacion);
            }

            // 4) notificarÉxito() / actualizar vista (solo logging aquí)
            System.out.println("[ControladorHabitos] notificarExito() para habitoId=" + habitoId);

            return habito;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtener registros de un hábito en un rango de fechas
     */
    public List<RegistroHabito> obtenerRegistros(Integer habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        return habitoDAO.findRegistrosByRango(habitoId, fechaInicio, fechaFin);
    }

    /**
     * Obtener todos los registros de hoy del usuario
     */
    public List<RegistroHabito> obtenerRegistrosDeHoy(Integer usuarioId) {
        try {
            return habitoDAO.findRegistrosDeHoy(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RegistroHabito>();
        }
    }

    /**
     * Buscar hábito por ID
     */
    public Habito buscarHabito(Integer habitoId) {
        return habitoDAO.findById(habitoId).orElse(null);
    }

    /**
     * Crear ficha de racha actual
     */
    public int crearFichaRacha(Habito habito, String estado, boolean cumplido) {
        if (habito != null) {
            return habito.calcularRachaActual();
        }
        return 0;
    }

    /**
     * Crear nuevo registro de hábito
     */
    public RegistroHabito crearNuevoRegistro(Habito habito, LocalDate fecha, String observacion) {
        try {
            // ✅ Actualizado a nueva estructura
            RegistroHabito registro = new RegistroHabito();
            registro.setHabito(habito);
            registro.setFecha(fecha);
            registro.setCompletado(true);  // ✅ Boolean
            registro.setVecesRealizado(1);  // ✅ Nuevo campo
            registro.setNotas(observacion);  // ✅ Cambiado a notas
            // guarda y retorna el registro (equivale a agregarRegistro(nuevoRegistro) en el diagrama)
            return habitoDAO.saveRegistro(registro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtener lista de hábitos del usuario
     */
    public List<Habito> listarHabitosUsuario(Integer usuarioId) {
        try {
            return habitoDAO.findByUsuarioId(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Habito>();
        }
    }

    /**
     * Guardar o actualizar hábito
     */
    public Habito guardarHabito(Habito habito) {
        try {
            return habitoDAO.save(habito);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Eliminar hábito (soft delete)
     */
    public boolean eliminarHabito(Integer habitoId) {
        try {
            habitoDAO.delete(habitoId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtener estadísticas del usuario
     */
    public Long obtenerHabitosCompletadosHoy(Integer usuarioId) {
        try {
            return habitoDAO.countHabitosCompletadosHoy(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Obtener porcentaje de completado de la semana
     */
    public Double obtenerPorcentajeSemana(Integer usuarioId) {
        try {
            return habitoDAO.getPorcentajeCompletadoSemana(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}