package protocol;

/**
 * Enum que define todos los tipos de acciones que puede realizar el cliente ElorES.
 * Cada acción corresponde a un Caso de Uso del proyecto.
 */
public enum ActionType {
    LOGIN,
    LOGOUT,
    GET_PUBLIC_KEY, 
    
    GET_PROFILE,
    UPDATE_PROFILE,
    
    GET_ALUMNOS,
    GET_ALUMNO_BY_ID,
    FILTER_ALUMNOS_BY_CICLO,
    FILTER_ALUMNOS_BY_MODULO,
    
    GET_MY_HORARIO,
    
    GET_HORARIO_BY_USER_ID,
    GET_HORARIOS_PROFESORES,
    
    CREATE_REUNION,
    GET_MY_REUNIONES,
    GET_REUNION_BY_ID,
    ACCEPT_REUNION,     
    CANCEL_REUNION,
    DELETE_REUNION,    
    
    PING,
    DISCONNECT
}
