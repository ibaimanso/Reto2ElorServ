package security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para cifrado y validación de contraseñas usando BCrypt
 * Compatible con la aplicación de escritorio ElorES
 */
public class PasswordUtil {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    /**
     * Cifra una contraseña en texto plano usando BCrypt
     * @param plainPassword Contraseña en texto plano
     * @return Hash BCrypt de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
    
    /**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt
     * @param plainPassword Contraseña en texto plano
     * @param hashedPassword Hash BCrypt almacenado en la base de datos
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            return encoder.matches(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al verificar contraseña: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si un string es un hash BCrypt válido
     * @param hash String a verificar
     * @return true si es un hash BCrypt válido
     */
    public static boolean isBCryptHash(String hash) {
        if (hash == null || hash.isEmpty()) {
            return false;
        }
        
        // BCrypt hashes comienzan con $2a$, $2b$, $2x$ o $2y$ y tienen longitud específica
        return hash.matches("^\\$2[abxy]\\$\\d{2}\\$.{53}$");
    }
}
