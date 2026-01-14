package protocol;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para las peticiones del cliente al servidor.
 * Se serializa/deserializa en JSON usando Gson.
 */
public class Request implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ActionType action;
    private String sessionToken;  // Token de sesión (null en LOGIN)
    private Map<String, Object> data;  // Datos variables según la acción
    private String timestamp;
    
    // Constructores
    public Request() {
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public Request(ActionType action, Map<String, Object> data) {
        this.action = action;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public Request(ActionType action, String sessionToken, Map<String, Object> data) {
        this.action = action;
        this.sessionToken = sessionToken;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    // Getters y Setters
    public ActionType getAction() {
        return action;
    }
    
    public void setAction(ActionType action) {
        this.action = action;
    }
    
    public String getSessionToken() {
        return sessionToken;
    }
    
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "action=" + action +
                ", sessionToken='" + (sessionToken != null ? "***" : "null") + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
