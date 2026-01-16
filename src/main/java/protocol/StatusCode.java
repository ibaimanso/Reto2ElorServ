package protocol;

public enum StatusCode {
    SUCCESS(200, "Operación exitosa"),
    CREATED(201, "Recurso creado exitosamente"),
    ACCEPTED(202, "Petición aceptada"),
    
    BAD_REQUEST(400, "Petición incorrecta o malformada"),
    UNAUTHORIZED(401, "No autorizado - credenciales inválidas"),
    FORBIDDEN(403, "Acceso denegado - permisos insuficientes"),
    NOT_FOUND(404, "Recurso no encontrado"),
    CONFLICT(409, "Conflicto - reunión en conflicto con horario"),
    
    INTERNAL_ERROR(500, "Error interno del servidor"),
    SERVICE_UNAVAILABLE(503, "Servicio no disponible");
    
    private final int code;
    private final String description;
    
    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
