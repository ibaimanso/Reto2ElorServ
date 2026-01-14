package protocol;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO para las respuestas del servidor al cliente.
 * Se serializa/deserializa en JSON usando Gson.
 */
public class Response implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private StatusCode status;
    private String message;
    private Object data;  // Puede ser un DTO, una lista, o cualquier objeto
    private String timestamp;
    
    // Constructores
    public Response() {
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public Response(StatusCode status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public Response(StatusCode status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    // Métodos estáticos para crear respuestas comunes
    public static Response success(String message, Object data) {
        return new Response(StatusCode.SUCCESS, message, data);
    }
    
    public static Response success(String message) {
        return new Response(StatusCode.SUCCESS, message);
    }
    
    public static Response error(String message) {
        return new Response(StatusCode.INTERNAL_ERROR, message);
    }
    
    public static Response unauthorized(String message) {
        return new Response(StatusCode.UNAUTHORIZED, message);
    }
    
    public static Response forbidden(String message) {
        return new Response(StatusCode.FORBIDDEN, message);
    }
    
    public static Response notFound(String message) {
        return new Response(StatusCode.NOT_FOUND, message);
    }
    
    public static Response badRequest(String message) {
        return new Response(StatusCode.BAD_REQUEST, message);
    }
    
    public static Response conflict(String message) {
        return new Response(StatusCode.CONFLICT, message);
    }
    
    // Getters y Setters
    public StatusCode getStatus() {
        return status;
    }
    
    public void setStatus(StatusCode status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
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
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
