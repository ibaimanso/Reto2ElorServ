package server;

import protocol.ActionType;
import protocol.Request;
import protocol.Response;
import protocol.StatusCode;
import security.SessionManager;
import service.*;
import dto.*;
import util.SocketLogger;
import util.RSAEncryptionUtil;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Procesador de peticiones del cliente.
 * Interpreta el tipo de acción y delega a los servicios correspondientes.
 * 
 * ARQUITECTURA EN CAPAS:
 * RequestProcessor → Service → Repository → Entity
 */
public class RequestProcessor {
    
    // Servicios inyectados
    private AuthService authService;
    private UserService userService;
    private HorarioService horarioService;
    private ReunionService reunionService;
    
    public RequestProcessor() {
        // Inicializar servicios
        this.authService = new AuthService();
        this.userService = new UserService();
        this.horarioService = new HorarioService();
        this.reunionService = new ReunionService();
    }
    
    /**
     * Procesa una petición del cliente y retorna una respuesta.
     */
    public Response processRequest(Request request) {
        try {
            ActionType action = request.getAction();
            
            // Acciones que NO requieren sesión
            if (action == ActionType.LOGIN || action == ActionType.GET_PUBLIC_KEY || action == ActionType.PING) {
                return processPublicAction(request);
            }
            
            // Todas las demás acciones requieren validación de sesión
            String sessionToken = request.getSessionToken();
            if (!SessionManager.validateSession(sessionToken)) {
                SocketLogger.warning("Intento de acceso sin sesión válida: " + action);
                return Response.unauthorized("Sesión inválida o expirada. Por favor, inicia sesión nuevamente.");
            }
            
            // Procesar según el tipo de acción
            switch (action) {
                case LOGOUT:
                    return handleLogout(request);
                
                // CU02 - Perfil
                case GET_PROFILE:
                    return handleGetProfile(request);
                case UPDATE_PROFILE:
                    return handleUpdateProfile(request);
                
                // CU03 - Alumnos (solo PROFESOR)
                case GET_ALUMNOS:
                    return handleGetAlumnos(request);
                case GET_ALUMNO_BY_ID:
                    return handleGetAlumnoById(request);
                case FILTER_ALUMNOS_BY_CICLO:
                    return handleFilterAlumnosByCiclo(request);
                case FILTER_ALUMNOS_BY_MODULO:
                    return handleFilterAlumnosByModulo(request);
                
                // CU04 - Horario propio
                case GET_MY_HORARIO:
                    return handleGetMyHorario(request);
                
                // CU05 - Horarios de otros
                case GET_HORARIO_BY_USER_ID:
                    return handleGetHorarioByUserId(request);
                case GET_HORARIOS_PROFESORES:
                    return handleGetHorariosProfesores(request);
                
                // CU06 y CU07 - Reuniones
                case CREATE_REUNION:
                    return handleCreateReunion(request);
                case GET_MY_REUNIONES:
                    return handleGetMyReuniones(request);
                case GET_REUNION_BY_ID:
                    return handleGetReunionById(request);
                case ACCEPT_REUNION:
                    return handleAcceptReunion(request);
                case CANCEL_REUNION:
                    return handleCancelReunion(request);
                case DELETE_REUNION:
                    return handleDeleteReunion(request);
                
                case DISCONNECT:
                    return handleDisconnect(request);
                
                default:
                    return Response.badRequest("Acción no reconocida: " + action);
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error al procesar petición", e);
            return Response.error("Error interno del servidor: " + e.getMessage());
        }
    }
    
    /**
     * Procesa acciones públicas (sin sesión).
     */
    private Response processPublicAction(Request request) {
        switch (request.getAction()) {
            case LOGIN:
                return handleLogin(request);
            case GET_PUBLIC_KEY:
                return handleGetPublicKey(request);
            case PING:
                return handlePing(request);
            default:
                return Response.badRequest("Acción no permitida");
        }
    }
    
    // ========== HANDLERS DE ACCIONES ==========
    
    /**
     * CU01 - Login con contraseña cifrada.
     */
    private Response handleLogin(Request request) {
        try {
            Map<String, Object> data = request.getData();
            String email = (String) data.get("email");
            String encryptedPassword = (String) data.get("encryptedPassword");
            
            if (email == null || encryptedPassword == null) {
                return Response.badRequest("Email y contraseña son requeridos");
            }
            
            // Descifrar contraseña con clave privada
            PrivateKey privateKey = RSAEncryptionUtil.loadPrivateKey();
            String plainPassword = RSAEncryptionUtil.decrypt(encryptedPassword, privateKey);
            
            // Llamar a AuthService.login
            UserDTO userDTO = authService.login(email, plainPassword);
            if (userDTO == null) {
                return Response.unauthorized("Credenciales inválidas");
            }
            
            // Crear sesión
            String sessionToken = SessionManager.createSession(
                userDTO.getId(), 
                userDTO.getEmail(), 
                userDTO.getTipoNombre()
            );
            
            // Preparar respuesta con usuario y token
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionToken", sessionToken);
            responseData.put("user", userDTO);
            
            SocketLogger.info("Login exitoso para: " + email);
            return Response.success("Login exitoso", responseData);
            
        } catch (Exception e) {
            SocketLogger.error("Error en login", e);
            return Response.error("Error al procesar login: " + e.getMessage());
        }
    }
    
    /**
     * Envía la clave pública RSA al cliente.
     */
    private Response handleGetPublicKey(Request request) {
        try {
            String publicKeyBase64 = RSAEncryptionUtil.getPublicKeyBase64();
            return Response.success("Clave pública RSA", publicKeyBase64);
        } catch (Exception e) {
            SocketLogger.error("Error al obtener clave pública", e);
            return Response.error("Error al obtener clave pública");
        }
    }
    
    /**
     * Ping para verificar conexión.
     */
    private Response handlePing(Request request) {
        return Response.success("Pong", System.currentTimeMillis());
    }
    
    /**
     * Logout - cierra sesión.
     */
    private Response handleLogout(Request request) {
        SessionManager.invalidateSession(request.getSessionToken());
        return Response.success("Sesión cerrada correctamente", null);
    }
    
    /**
     * Desconexión del cliente.
     */
    private Response handleDisconnect(Request request) {
        SessionManager.invalidateSession(request.getSessionToken());
        return Response.success("Desconexión exitosa", null);
    }
    
    // ========== CU02 - PERFIL ==========
    
    private Response handleGetProfile(Request request) {
        Integer userId = SessionManager.getUserId(request.getSessionToken());
        UserDTO userDTO = userService.getUserById(userId);
        
        if (userDTO == null) {
            return Response.notFound("Usuario no encontrado");
        }
        
        return Response.success("Perfil obtenido", userDTO);
    }
    
    private Response handleUpdateProfile(Request request) {
        try {
            Map<String, Object> data = request.getData();
            Integer userId = SessionManager.getUserId(request.getSessionToken());
            
            // Crear DTO con datos actualizados
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setNombre((String) data.get("nombre"));
            userDTO.setApellidos((String) data.get("apellidos"));
            userDTO.setDireccion((String) data.get("direccion"));
            userDTO.setTelefono1((String) data.get("telefono1"));
            userDTO.setTelefono2((String) data.get("telefono2"));
            userDTO.setArgazkiaUrl((String) data.get("argazkiaUrl"));
            
            boolean updated = userService.updateUser(userDTO);
            
            if (updated) {
                return Response.success("Perfil actualizado correctamente", null);
            } else {
                return Response.error("Error al actualizar perfil");
            }
        } catch (Exception e) {
            SocketLogger.error("Error al actualizar perfil", e);
            return Response.error("Error al actualizar perfil");
        }
    }
    
    // ========== CU03 - ALUMNOS (solo PROFESOR) ==========
    
    private Response handleGetAlumnos(Request request) {
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        if (!"PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            return Response.forbidden("Solo los profesores pueden consultar alumnos");
        }
        
        List<UserDTO> alumnos = userService.getAllAlumnos();
        return Response.success("Lista de alumnos obtenida", alumnos);
    }
    
    private Response handleGetAlumnoById(Request request) {
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        if (!"PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            return Response.forbidden("Solo los profesores pueden consultar alumnos");
        }
        
        try {
            Map<String, Object> data = request.getData();
            Integer alumnoId = ((Number) data.get("alumnoId")).intValue();
            
            UserDTO alumno = userService.getUserById(alumnoId);
            if (alumno == null) {
                return Response.notFound("Alumno no encontrado");
            }
            
            return Response.success("Alumno obtenido", alumno);
        } catch (Exception e) {
            return Response.badRequest("ID de alumno inválido");
        }
    }
    
    private Response handleFilterAlumnosByCiclo(Request request) {
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        if (!"PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            return Response.forbidden("Solo los profesores pueden consultar alumnos");
        }
        
        try {
            Map<String, Object> data = request.getData();
            Integer cicloId = ((Number) data.get("cicloId")).intValue();
            
            List<UserDTO> alumnos = userService.filterAlumnosByCiclo(cicloId);
            return Response.success("Alumnos filtrados por ciclo", alumnos);
        } catch (Exception e) {
            return Response.badRequest("Datos de filtro inválidos");
        }
    }
    
    private Response handleFilterAlumnosByModulo(Request request) {
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        if (!"PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            return Response.forbidden("Solo los profesores pueden consultar alumnos");
        }
        
        try {
            Map<String, Object> data = request.getData();
            Integer moduloId = ((Number) data.get("moduloId")).intValue();
            
            List<UserDTO> alumnos = userService.filterAlumnosByModulo(moduloId);
            return Response.success("Alumnos filtrados por módulo", alumnos);
        } catch (Exception e) {
            return Response.badRequest("Datos de filtro inválidos");
        }
    }
    
    // ========== CU04 - HORARIO PROPIO ==========
    
    private Response handleGetMyHorario(Request request) {
        Integer userId = SessionManager.getUserId(request.getSessionToken());
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        
        List<HorarioDTO> horarios = horarioService.getHorarioByUserId(userId, tipoUsuario);
        return Response.success("Horario obtenido", horarios);
    }
    
    // ========== CU05 - HORARIOS DE OTROS ==========
    
    private Response handleGetHorarioByUserId(Request request) {
        try {
            Map<String, Object> data = request.getData();
            Integer userId = ((Number) data.get("userId")).intValue();
            String tipoUsuario = (String) data.get("tipoUsuario");
            
            List<HorarioDTO> horarios = horarioService.getHorarioByUserId(userId, tipoUsuario);
            return Response.success("Horario obtenido", horarios);
        } catch (Exception e) {
            return Response.badRequest("Datos inválidos para consultar horario");
        }
    }
    
    private Response handleGetHorariosProfesores(Request request) {
        List<UserDTO> profesores = userService.getAllProfesores();
        return Response.success("Lista de profesores obtenida", profesores);
    }
    
    // ========== CU06 y CU07 - REUNIONES ==========
    
    private Response handleCreateReunion(Request request) {
        try {
            Map<String, Object> data = request.getData();
            
            // Crear DTO desde los datos recibidos
            ReunionDTO reunionDTO = new ReunionDTO();
            reunionDTO.setProfesorId(((Number) data.get("profesorId")).intValue());
            reunionDTO.setAlumnoId(((Number) data.get("alumnoId")).intValue());
            reunionDTO.setTitulo((String) data.get("titulo"));
            reunionDTO.setAsunto((String) data.get("asunto"));
            reunionDTO.setAula((String) data.get("aula"));
            reunionDTO.setIdCentro((String) data.get("idCentro"));
            
            // Parsear fecha (asumiendo formato ISO)
            String fechaStr = (String) data.get("fecha");
            reunionDTO.setFecha(java.time.LocalDateTime.parse(fechaStr));
            
            ReunionDTO created = reunionService.createReunion(reunionDTO);
            
            if (created != null) {
                return new Response(StatusCode.CREATED, "Reunión creada exitosamente", created);
            } else {
                return Response.error("Error al crear reunión");
            }
        } catch (Exception e) {
            SocketLogger.error("Error al crear reunión", e);
            return Response.badRequest("Datos de reunión inválidos: " + e.getMessage());
        }
    }
    
    private Response handleGetMyReuniones(Request request) {
        Integer userId = SessionManager.getUserId(request.getSessionToken());
        List<ReunionDTO> reuniones = reunionService.getReunionsByUserId(userId);
        return Response.success("Reuniones obtenidas", reuniones);
    }
    
    private Response handleGetReunionById(Request request) {
        try {
            Map<String, Object> data = request.getData();
            Integer reunionId = ((Number) data.get("reunionId")).intValue();
            
            // TODO: Implementar método en ReunionService
            return Response.success("Reunión obtenida - implementar getById", null);
        } catch (Exception e) {
            return Response.badRequest("ID de reunión inválido");
        }
    }
    
    private Response handleAcceptReunion(Request request) {
        String tipoUsuario = SessionManager.getUserType(request.getSessionToken());
        if (!"PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            return Response.forbidden("Solo los profesores pueden aceptar reuniones");
        }
        
        try {
            Map<String, Object> data = request.getData();
            Integer reunionId = ((Number) data.get("reunionId")).intValue();
            Integer profesorId = SessionManager.getUserId(request.getSessionToken());
            
            boolean accepted = reunionService.acceptReunion(reunionId, profesorId);
            
            if (accepted) {
                return new Response(StatusCode.ACCEPTED, "Reunión aceptada", null);
            } else {
                return Response.error("Error al aceptar reunión");
            }
        } catch (Exception e) {
            return Response.badRequest("Datos inválidos");
        }
    }
    
    private Response handleCancelReunion(Request request) {
        try {
            Map<String, Object> data = request.getData();
            Integer reunionId = ((Number) data.get("reunionId")).intValue();
            Integer userId = SessionManager.getUserId(request.getSessionToken());
            
            boolean cancelled = reunionService.cancelReunion(reunionId, userId);
            
            if (cancelled) {
                return Response.success("Reunión cancelada", null);
            } else {
                return Response.error("Error al cancelar reunión");
            }
        } catch (Exception e) {
            return Response.badRequest("Datos inválidos");
        }
    }
    
    private Response handleDeleteReunion(Request request) {
        try {
            Map<String, Object> data = request.getData();
            Integer reunionId = ((Number) data.get("reunionId")).intValue();
            
            boolean deleted = reunionService.deleteReunion(reunionId);
            
            if (deleted) {
                return Response.success("Reunión eliminada", null);
            } else {
                return Response.error("Error al eliminar reunión");
            }
        } catch (Exception e) {
            return Response.badRequest("Datos inválidos");
        }
    }
}
