package com.example.services;

import com.example.dao.HabitoDAO;
import com.example.model.Habito;
import com.example.model.RegistroHabito;
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
    public Habito registrarCumplimiento(Long habitoId, LocalDate fecha, String observacion) {
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
    public List<RegistroHabito> obtenerRegistros(Long habitoId, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            return habitoDAO.findRegistrosByRango(habitoId, fechaInicio, fechaFin);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<RegistroHabito>();
        }
    }

    /**
     * Obtener todos los registros de hoy del usuario
     */
    public List<RegistroHabito> obtenerRegistrosDeHoy(String usuarioId) {
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
    public Habito buscarHabito(Long habitoId) {
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
            RegistroHabito registro = new RegistroHabito(habito, fecha, 1, observacion);
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
    public List<Habito> listarHabitosUsuario(String usuarioId) {
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
    public boolean eliminarHabito(Long habitoId) {
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
    public Long obtenerHabitosCompletadosHoy(String usuarioId) {
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
    public Double obtenerPorcentajeSemana(String usuarioId) {
        try {
            return habitoDAO.getPorcentajeCompletadoSemana(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}