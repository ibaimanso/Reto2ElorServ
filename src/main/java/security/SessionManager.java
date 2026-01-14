package security;

import util.SocketLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de sesiones de usuarios conectados.
 * Thread-safe usando ConcurrentHashMap.
 */
public class SessionManager {
    
    private static final int SESSION_TIMEOUT_MINUTES = 60;  // 60 minutos de inactividad
    private static final Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();
    
    /**
     * Crea una nueva sesión para un usuario.
     * @return El token de sesión generado
     */
    public static String createSession(Integer userId, String email, String tipoUsuario) {
        String sessionToken = UUID.randomUUID().toString();
        UserSession session = new UserSession(sessionToken, userId, email, tipoUsuario);
        activeSessions.put(sessionToken, session);
        
        SocketLogger.info("Sesión creada para usuario: " + email + " (ID: " + userId + ")");
        return sessionToken;
    }
    
    /**
     * Valida si un token de sesión es válido y está activo.
     */
    public static boolean validateSession(String sessionToken) {
        if (sessionToken == null || sessionToken.isEmpty()) {
            return false;
        }
        
        UserSession session = activeSessions.get(sessionToken);
        if (session == null) {
            return false;
        }
        
        // Verificar si la sesión ha expirado
        if (!session.isActive(SESSION_TIMEOUT_MINUTES)) {
            invalidateSession(sessionToken);
            SocketLogger.warning("Sesión expirada para token: " + sessionToken);
            return false;
        }
        
        // Actualizar última actividad
        session.updateActivity();
        return true;
    }
    
    /**
     * Obtiene la sesión de usuario por token.
     */
    public static UserSession getSession(String sessionToken) {
        if (validateSession(sessionToken)) {
            return activeSessions.get(sessionToken);
        }
        return null;
    }
    
    /**
     * Obtiene el ID de usuario por token.
     */
    public static Integer getUserId(String sessionToken) {
        UserSession session = getSession(sessionToken);
        return session != null ? session.getUserId() : null;
    }
    
    /**
     * Obtiene el tipo de usuario por token.
     */
    public static String getUserType(String sessionToken) {
        UserSession session = getSession(sessionToken);
        return session != null ? session.getTipoUsuario() : null;
    }
    
    /**
     * Invalida (cierra) una sesión.
     */
    public static void invalidateSession(String sessionToken) {
        UserSession session = activeSessions.remove(sessionToken);
        if (session != null) {
            SocketLogger.info("Sesión cerrada para usuario ID: " + session.getUserId());
        }
    }
    
    /**
     * Obtiene el número de sesiones activas.
     */
    public static int getActiveSessionsCount() {
        return activeSessions.size();
    }
    
    /**
     * Limpia todas las sesiones (usado al apagar el servidor).
     */
    public static void clearAllSessions() {
        int count = activeSessions.size();
        activeSessions.clear();
        SocketLogger.info("Se cerraron " + count + " sesiones activas.");
    }
    
    /**
     * Limpia sesiones expiradas (puede ejecutarse periódicamente).
     */
    public static void cleanExpiredSessions() {
        int removedCount = 0;
        for (Map.Entry<String, UserSession> entry : activeSessions.entrySet()) {
            if (!entry.getValue().isActive(SESSION_TIMEOUT_MINUTES)) {
                activeSessions.remove(entry.getKey());
                removedCount++;
            }
        }
        if (removedCount > 0) {
            SocketLogger.info("Se eliminaron " + removedCount + " sesiones expiradas.");
        }
    }
}
