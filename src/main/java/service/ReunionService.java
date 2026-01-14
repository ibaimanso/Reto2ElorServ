package service;

import dto.ReunionDTO;
import entidades.Reuniones;
import entidades.Users;
import repository.ReunionRepository;
import repository.UserRepository;
import util.SocketLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de reuniones.
 * CU06 - Crear reuniones
 * CU07 - Gestionar reuniones (aceptar/cancelar)
 */
public class ReunionService {
    
    private ReunionRepository reunionRepository;
    private UserRepository userRepository;
    // TODO: Inyectar EmailService cuando se cree
    // private EmailService emailService;
    
    public ReunionService() {
        this.reunionRepository = new ReunionRepository();
        this.userRepository = new UserRepository();
    }
    
    /**
     * Crea una nueva reunión.
     * CU06 - Crear reunión
     * 
     * 1. Valida los datos
     * 2. Verifica conflictos de horario
     * 3. Si hay conflicto → estado CONFLICTO
     * 4. Si no → estado PENDIENTE
     * 5. Envía correo automático
     */
    public ReunionDTO createReunion(ReunionDTO reunionDTO) {
        try {
            // Validar datos básicos
            if (reunionDTO.getProfesorId() == null || reunionDTO.getAlumnoId() == null) {
                SocketLogger.error("Datos incompletos para crear reunión");
                return null;
            }
            
            // Buscar profesor y alumno
            Users profesor = userRepository.findById(reunionDTO.getProfesorId());
            Users alumno = userRepository.findById(reunionDTO.getAlumnoId());
            
            if (profesor == null || alumno == null) {
                SocketLogger.error("Profesor o alumno no encontrado");
                return null;
            }
            
            // Crear entidad Reuniones
            Reuniones reunion = new Reuniones();
            reunion.setTitulo(reunionDTO.getTitulo());
            reunion.setAsunto(reunionDTO.getAsunto());
            reunion.setAula(reunionDTO.getAula());
            reunion.setFecha(reunionDTO.getFecha());
            reunion.setIdCentro(reunionDTO.getIdCentro());
            reunion.setProfesor(profesor);
            reunion.setAlumno(alumno);
            
            // Verificar conflictos de horario
            boolean hasConflict = checkConflicts(
                reunionDTO.getProfesorId(), 
                reunionDTO.getFecha()
            );
            
            if (hasConflict) {
                reunion.setEstado("CONFLICTO");
                reunion.setEstadoEus("Gatazka");
                SocketLogger.warning("Reunión creada con CONFLICTO de horario");
            } else {
                reunion.setEstado("PENDIENTE");
                reunion.setEstadoEus("Zain");
                SocketLogger.info("Reunión creada en estado PENDIENTE");
            }
            
            // Guardar en BD
            Reuniones savedReunion = reunionRepository.save(reunion);
            
            if (savedReunion != null) {
                // TODO: Enviar correo automático
                // emailService.sendReunionNotification(savedReunion);
                SocketLogger.info("Correo de notificación pendiente de implementar");
                
                return convertToDTO(savedReunion);
            }
            
            return null;
            
        } catch (Exception e) {
            SocketLogger.error("Error al crear reunión", e);
            return null;
        }
    }
    
    /**
     * Verifica si hay conflictos de horario para una reunión.
     */
    private boolean checkConflicts(Integer profesorId, LocalDateTime fecha) {
        try {
            // Buscar reuniones en un rango de ±1 hora
            LocalDateTime startRange = fecha.minusHours(1);
            LocalDateTime endRange = fecha.plusHours(1);
            
            List<Reuniones> reunionesExistentes = 
                reunionRepository.findByProfesorAndDateRange(profesorId, startRange, endRange);
            
            return reunionesExistentes != null && !reunionesExistentes.isEmpty();
            
        } catch (Exception e) {
            SocketLogger.error("Error al verificar conflictos", e);
            return false;
        }
    }
    
    /**
     * Obtiene todas las reuniones de un usuario.
     * CU06/CU07 - Ver mis reuniones
     */
    public List<ReunionDTO> getReunionsByUserId(Integer userId) {
        try {
            List<Reuniones> reuniones = reunionRepository.findByUserId(userId);
            if (reuniones == null) {
                return new ArrayList<>();
            }
            
            return reuniones.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            SocketLogger.error("Error al obtener reuniones del usuario", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Acepta una reunión (solo PROFESOR).
     * CU07 - Aceptar reunión
     */
    public boolean acceptReunion(Integer reunionId, Integer profesorId) {
        try {
            Reuniones reunion = reunionRepository.findById(reunionId);
            
            if (reunion == null) {
                SocketLogger.error("Reunión no encontrada: " + reunionId);
                return false;
            }
            
            // Verificar que el profesor es el de la reunión
            if (!reunion.getProfesor().getId().equals(profesorId)) {
                SocketLogger.error("Profesor no autorizado para aceptar esta reunión");
                return false;
            }
            
            // Cambiar estado
            reunion.setEstado("ACEPTADA");
            reunion.setEstadoEus("Onartuta");
            
            boolean updated = reunionRepository.update(reunion);
            
            if (updated) {
                // TODO: Enviar correo de notificación
                // emailService.sendReunionStatusChange(reunion);
                SocketLogger.info("Reunión aceptada: " + reunionId);
            }
            
            return updated;
            
        } catch (Exception e) {
            SocketLogger.error("Error al aceptar reunión", e);
            return false;
        }
    }
    
    /**
     * Cancela una reunión.
     * CU07 - Cancelar reunión
     */
    public boolean cancelReunion(Integer reunionId, Integer userId) {
        try {
            Reuniones reunion = reunionRepository.findById(reunionId);
            
            if (reunion == null) {
                return false;
            }
            
            // Verificar que el usuario está involucrado en la reunión
            boolean isProfesor = reunion.getProfesor().getId().equals(userId);
            boolean isAlumno = reunion.getAlumno().getId().equals(userId);
            
            if (!isProfesor && !isAlumno) {
                SocketLogger.error("Usuario no autorizado para cancelar esta reunión");
                return false;
            }
            
            // Cambiar estado
            reunion.setEstado("CANCELADA");
            reunion.setEstadoEus("Bertan behera");
            
            boolean updated = reunionRepository.update(reunion);
            
            if (updated) {
                // TODO: Enviar correo de notificación
                SocketLogger.info("Reunión cancelada: " + reunionId);
            }
            
            return updated;
            
        } catch (Exception e) {
            SocketLogger.error("Error al cancelar reunión", e);
            return false;
        }
    }
    
    /**
     * Elimina una reunión (opcional).
     */
    public boolean deleteReunion(Integer reunionId) {
        try {
            return reunionRepository.delete(reunionId);
        } catch (Exception e) {
            SocketLogger.error("Error al eliminar reunión", e);
            return false;
        }
    }
    
    /**
     * Convierte una entidad Reuniones a ReunionDTO.
     */
    private ReunionDTO convertToDTO(Reuniones reunion) {
        ReunionDTO dto = new ReunionDTO();
        dto.setIdReunion(reunion.getIdReunion());
        dto.setEstado(reunion.getEstado());
        dto.setEstadoEus(reunion.getEstadoEus());
        dto.setIdCentro(reunion.getIdCentro());
        dto.setTitulo(reunion.getTitulo());
        dto.setAsunto(reunion.getAsunto());
        dto.setAula(reunion.getAula());
        dto.setFecha(reunion.getFecha());
        dto.setCreatedAt(reunion.getCreatedAt());
        dto.setUpdatedAt(reunion.getUpdatedAt());
        
        // Información del profesor
        if (reunion.getProfesor() != null) {
            dto.setProfesorId(reunion.getProfesor().getId());
            dto.setProfesorNombre(reunion.getProfesor().getNombre());
            dto.setProfesorApellidos(reunion.getProfesor().getApellidos());
            dto.setProfesorEmail(reunion.getProfesor().getEmail());
        }
        
        // Información del alumno
        if (reunion.getAlumno() != null) {
            dto.setAlumnoId(reunion.getAlumno().getId());
            dto.setAlumnoNombre(reunion.getAlumno().getNombre());
            dto.setAlumnoApellidos(reunion.getAlumno().getApellidos());
            dto.setAlumnoEmail(reunion.getAlumno().getEmail());
        }
        
        return dto;
    }
}
