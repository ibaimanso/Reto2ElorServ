package service;

import dto.UserDTO;
import entidades.Users;
import repository.UserRepository;
import util.SocketLogger;

/**
 * Servicio de autenticación.
 * Gestiona login y validación de usuarios.
 */
public class AuthService {
    
    private UserRepository userRepository;
    
    public AuthService() {
        this.userRepository = new UserRepository();
    }
    
    /**
     * Realiza el login de un usuario.
     * La contraseña ya viene descifrada desde el RequestProcessor.
     * 
     * @param email Email del usuario
     * @param plainPassword Contraseña en texto plano (ya descifrada)
     * @return UserDTO si las credenciales son válidas, null si no
     */
    public UserDTO login(String email, String plainPassword) {
        try {
            // Buscar usuario por email
            Users user = userRepository.findByEmail(email);
            
            if (user == null) {
                SocketLogger.warning("Intento de login con email inexistente: " + email);
                return null;
            }
            
            // Validar contraseña
            // IMPORTANTE: Aquí deberías usar BCrypt para comparar
            // Por ahora, comparación directa (CAMBIAR EN PRODUCCIÓN)
            if (!user.getPassword().equals(plainPassword)) {
                SocketLogger.warning("Contraseña incorrecta para: " + email);
                return null;
            }
            
            // TODO: Si usas BCrypt, descomentar:
            // if (!BCrypt.checkpw(plainPassword, user.getPassword())) {
            //     return null;
            // }
            
            // Convertir a DTO
            UserDTO userDTO = convertToDTO(user);
            
            SocketLogger.info("Login exitoso para: " + email + " (Tipo: " + userDTO.getTipoNombre() + ")");
            return userDTO;
            
        } catch (Exception e) {
            SocketLogger.error("Error en login", e);
            return null;
        }
    }
    
    /**
     * Valida que un usuario tenga un tipo específico.
     */
    public boolean validateUserType(Integer userId, String expectedType) {
        Users user = userRepository.findById(userId);
        if (user == null || user.getTipo() == null) {
            return false;
        }
        return user.getTipo().getName().equalsIgnoreCase(expectedType);
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
