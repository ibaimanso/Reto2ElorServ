package service;

import dto.UserDTO;
import entidades.Users;
import repository.UserRepository;
import util.SocketLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de usuarios.
 * CU02 - Perfil
 * CU03 - Consultar alumnos
 */
public class UserService {
    
    private UserRepository userRepository;
    
    public UserService() {
        this.userRepository = new UserRepository();
    }
    
    /**
     * Obtiene el perfil de un usuario por ID.
     * CU02 - Ver perfil
     */
    public UserDTO getUserById(Integer userId) {
        try {
            Users user = userRepository.findById(userId);
            if (user == null) {
                return null;
            }
            return convertToDTO(user);
        } catch (Exception e) {
            SocketLogger.error("Error al obtener perfil de usuario", e);
            return null;
        }
    }
    
    /**
     * Actualiza el perfil de un usuario.
     * CU02 - Actualizar perfil
     */
    public boolean updateUser(UserDTO userDTO) {
        try {
            Users user = userRepository.findById(userDTO.getId());
            if (user == null) {
                return false;
            }
            
            // Actualizar campos permitidos (NO la contraseña aquí)
            user.setNombre(userDTO.getNombre());
            user.setApellidos(userDTO.getApellidos());
            user.setDireccion(userDTO.getDireccion());
            user.setTelefono1(userDTO.getTelefono1());
            user.setTelefono2(userDTO.getTelefono2());
            user.setArgazkiaUrl(userDTO.getArgazkiaUrl());
            
            return userRepository.update(user);
            
        } catch (Exception e) {
            SocketLogger.error("Error al actualizar usuario", e);
            return false;
        }
    }
    
    /**
     * Obtiene todos los alumnos.
     * CU03 - Consultar alumnos (solo PROFESOR)
     */
    public List<UserDTO> getAllAlumnos() {
        try {
            List<Users> alumnos = userRepository.findAllAlumnos();
            if (alumnos == null) {
                return new ArrayList<>();
            }
            
            return alumnos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            SocketLogger.error("Error al obtener alumnos", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene todos los profesores.
     * CU05 - Ver horarios de profesores
     */
    public List<UserDTO> getAllProfesores() {
        try {
            List<Users> profesores = userRepository.findAllProfesores();
            if (profesores == null) {
                return new ArrayList<>();
            }
            
            return profesores.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            SocketLogger.error("Error al obtener profesores", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Filtra alumnos por ciclo.
     * CU03 - Filtrar alumnos
     */
    public List<UserDTO> filterAlumnosByCiclo(Integer cicloId) {
        // TODO: Implementar filtro específico con consulta HQL
        SocketLogger.info("Filtrado de alumnos por ciclo: " + cicloId);
        return new ArrayList<>();
    }
    
    /**
     * Filtra alumnos por módulo.
     * CU03 - Filtrar alumnos
     */
    public List<UserDTO> filterAlumnosByModulo(Integer moduloId) {
        // TODO: Implementar filtro específico con consulta HQL
        SocketLogger.info("Filtrado de alumnos por módulo: " + moduloId);
        return new ArrayList<>();
    }
    
    /**
     * Convierte una entidad Users a UserDTO.
     */
    private UserDTO convertToDTO(Users user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setNombre(user.getNombre());
        dto.setApellidos(user.getApellidos());
        dto.setDni(user.getDni());
        dto.setDireccion(user.getDireccion());
        dto.setTelefono1(user.getTelefono1());
        dto.setTelefono2(user.getTelefono2());
        dto.setArgazkiaUrl(user.getArgazkiaUrl());
        
        if (user.getTipo() != null) {
            dto.setTipoId(user.getTipo().getId().toString());
            dto.setTipoNombre(user.getTipo().getName());
        }
        
        return dto;
    }
}
