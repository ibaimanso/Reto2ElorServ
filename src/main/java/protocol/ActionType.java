package protocol;

/**
 * Enum que define todos los tipos de acciones que puede realizar el cliente ElorES.
 * Cada acción corresponde a un Caso de Uso del proyecto.
 */
public enum ActionType {
    // Autenticación
    LOGIN,
    LOGOUT,
    GET_PUBLIC_KEY,  // El cliente solicita la clave pública para cifrar
    
    // CU02 - Perfil
    GET_PROFILE,
    UPDATE_PROFILE,
    
    // CU03 - Consultar alumnos (solo PROFESOR)
    GET_ALUMNOS,
    GET_ALUMNO_BY_ID,
    FILTER_ALUMNOS_BY_CICLO,
    FILTER_ALUMNOS_BY_MODULO,
    
    // CU04 - Ver horario propio
    GET_MY_HORARIO,
    
    // CU05 - Ver horarios de otros
    GET_HORARIO_BY_USER_ID,
    GET_HORARIOS_PROFESORES,
    
    // CU06 y CU07 - Gestión de reuniones
    CREATE_REUNION,
    GET_MY_REUNIONES,
    GET_REUNION_BY_ID,
    ACCEPT_REUNION,      // Solo PROFESOR
    CANCEL_REUNION,
    DELETE_REUNION,      // Opcional
    
    // Utilidades
    PING,
    DISCONNECT
}
