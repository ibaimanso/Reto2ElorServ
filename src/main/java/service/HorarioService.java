package service;

import dto.HorarioDTO;
import entidades.Horarios;
import repository.HorarioRepository;
import util.SocketLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de horarios.
 * CU04 - Ver horario propio
 * CU05 - Ver horarios de otros
 */
public class HorarioService {
    
    private HorarioRepository horarioRepository;
    
    public HorarioService() {
        this.horarioRepository = new HorarioRepository();
    }
    
    /**
     * Obtiene el horario de un usuario.
     * Si es PROFESOR: sus clases
     * Si es ALUMNO: clases de sus profesores según matrículas
     * 
     * CU04 - Ver mi horario
     */
    public List<HorarioDTO> getHorarioByUserId(Integer userId, String tipoUsuario) {
        try {
            List<Horarios> horarios;
            
            if ("PROFESOR".equalsIgnoreCase(tipoUsuario)) {
                horarios = horarioRepository.findByProfesorId(userId);
            } else if ("ALUMNO".equalsIgnoreCase(tipoUsuario)) {
                horarios = horarioRepository.findByAlumnoId(userId);
            } else {
                SocketLogger.warning("Tipo de usuario no válido para horarios: " + tipoUsuario);
                return new ArrayList<>();
            }
            
            if (horarios == null) {
                return new ArrayList<>();
            }
            
            return horarios.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            SocketLogger.error("Error al obtener horario del usuario", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene todos los horarios (para administración).
     */
    public List<HorarioDTO> getAllHorarios() {
        try {
            List<Horarios> horarios = horarioRepository.findAll();
            if (horarios == null) {
                return new ArrayList<>();
            }
            
            return horarios.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los horarios", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Convierte una entidad Horarios a HorarioDTO.
     */
    private HorarioDTO convertToDTO(Horarios horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(horario.getId());
        dto.setDia(horario.getDia());
        dto.setHora(horario.getHora());
        dto.setAula(horario.getAula());
        dto.setObservaciones(horario.getObservaciones());
        
        // Información del profesor (si existe)
        if (horario.getProfesor() != null) {
            dto.setProfesorId(horario.getProfesor().getId());
            dto.setProfesorNombre(horario.getProfesor().getNombre());
            dto.setProfesorApellidos(horario.getProfesor().getApellidos());
        }
        
        // Información del módulo (si existe)
        if (horario.getModulo() != null) {
            dto.setModuloId(horario.getModulo().getId());
            dto.setModuloNombre(horario.getModulo().getNombre());
            dto.setModuloNombreEus(horario.getModulo().getNombreEus());
        }
        
        return dto;
    }
}
