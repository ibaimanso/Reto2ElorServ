package security;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa una sesión activa de un usuario conectado.
 */
public class UserSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sessionToken;
    private Integer userId;
    private String email;
    private String tipoUsuario;  // PROFESOR, ALUMNO
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    
    public UserSession(String sessionToken, Integer userId, String email, String tipoUsuario) {
        this.sessionToken = sessionToken;
        this.userId = userId;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.loginTime = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
    }
    
    /**
     * Actualiza el timestamp de última actividad.
     */
    public void updateActivity() {
        this.lastActivity = LocalDateTime.now();
    }
    
    /**
     * Verifica si la sesión está activa (no expirada).
     * @param timeoutMinutes Minutos de inactividad permitidos
     */
    public boolean isActive(int timeoutMinutes) {
        LocalDateTime expirationTime = lastActivity.plusMinutes(timeoutMinutes);
        return LocalDateTime.now().isBefore(expirationTime);
    }
    
    // Getters y Setters
    public String getSessionToken() {
        return sessionToken;
    }
    
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
    
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    
    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    @Override
    public String toString() {
        return "UserSession{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }
}
