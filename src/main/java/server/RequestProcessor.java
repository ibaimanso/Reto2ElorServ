package server;

import protocol.Request;
import protocol.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import util.HibernateUtil;
import util.SocketLogger;
import security.PasswordUtil;

public class RequestProcessor {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public Response processRequest(Request request) {
        try {
            switch (request.getAction()) {
                case LOGIN:
                    return handleLogin(request);
                case GET_PERFIL:
                    return handleGetPerfil(request);
                case DISCONNECT:
                    return handleDisconnect(request);
                default:
                    return Response.success("Acción procesada: " + request.getAction(), null);
            }
        } catch (Exception e) {
            SocketLogger.error("Error procesando request: " + request.getAction(), e);
            return Response.error("Error interno del servidor: " + e.getMessage());
        }
    }
    
    private Response handleLogin(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO LOGIN ===");
            
            // Parsear payload con Jackson
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("email") || !payload.has("password")) {
                SocketLogger.warning("Faltan campos email o password");
                return Response.badRequest("Email y password son requeridos");
            }
            
            String email = payload.get("email").asText();
            String password = payload.get("password").asText();
            
            SocketLogger.info("Intento de login: " + email);
            
            // Buscar usuario en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query para obtener datos del usuario
                Object[] result = (Object[]) session.createNativeQuery(
                    "SELECT id, email, username, nombre, apellidos, password, tipo_id FROM users WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();
                
                if (result == null) {
                    SocketLogger.warning("Usuario no encontrado: " + email);
                    return Response.unauthorized("Credenciales inválidas");
                }
                
                // Extraer datos
                int id = ((Number) result[0]).intValue();
                String dbEmail = (String) result[1];
                String username = (String) result[2];
                String nombre = (String) result[3];
                String apellidos = (String) result[4];
                String hashedPassword = (String) result[5];
                int tipoId = ((Number) result[6]).intValue();
                
                SocketLogger.info("Usuario encontrado en BD: " + username + " (tipo_id=" + tipoId + ")");
                
                // VALIDAR CONTRASEÑA CON BCRYPT
                if (!PasswordUtil.verifyPassword(password, hashedPassword)) {
                    SocketLogger.warning("Contraseña incorrecta para usuario: " + email);
                    return Response.unauthorized("Credenciales inválidas");
                }
                
                SocketLogger.info("Contraseña validada correctamente");
                
                // Verificar que es profesor (tipo_id = 3)
                if (tipoId != 3) {
                    SocketLogger.warning("Acceso denegado - no es profesor: " + email + " (tipo_id=" + tipoId + ")");
                    return Response.forbidden("Acceso denegado. Solo profesores pueden usar esta aplicación.");
                }
                
                // Crear respuesta con datos del usuario (SIN contraseña)
                String userData = String.format(
                    "{\"id\":%d,\"email\":\"%s\",\"username\":\"%s\",\"nombre\":\"%s\",\"apellidos\":\"%s\",\"tipoId\":%d,\"tipoNombre\":\"profesor\"}",
                    id, dbEmail, username != null ? username : "", nombre != null ? nombre : "", apellidos != null ? apellidos : "", tipoId
                );
                
                SocketLogger.info("Login exitoso: " + email);
                
                return Response.success("Autenticación exitosa", userData);
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleLogin", e);
            e.printStackTrace();
            return Response.error("Error en autenticación: " + e.getMessage());
        }
    }
    
    private Response handleDisconnect(Request request) {
        return Response.success("Desconexión exitosa", null);
    }
    
    private Response handleGetPerfil(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO GET_PERFIL ===");
            
            // Parsear payload con Jackson
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("userId")) {
                SocketLogger.warning("Falta campo userId");
                return Response.badRequest("userId es requerido");
            }
            
            int userId = payload.get("userId").asInt();
            SocketLogger.info("Obteniendo perfil del usuario ID: " + userId);
            
            // Buscar usuario en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query para obtener datos completos del usuario
                Object[] result = (Object[]) session.createNativeQuery(
                    "SELECT u.id, u.email, u.username, u.nombre, u.apellidos, u.dni, " +
                    "u.direccion, u.telefono1, u.telefono2, u.tipo_id, u.argazkia_url " +
                    "FROM users u WHERE u.id = :userId")
                    .setParameter("userId", userId)
                    .uniqueResult();
                
                if (result == null) {
                    SocketLogger.warning("Usuario no encontrado: ID " + userId);
                    return Response.error("Perfil no encontrado");
                }
                
                // Extraer datos
                int id = ((Number) result[0]).intValue();
                String email = (String) result[1];
                String username = (String) result[2];
                String nombre = (String) result[3];
                String apellidos = (String) result[4];
                String dni = result[5] != null ? (String) result[5] : "";
                String direccion = result[6] != null ? (String) result[6] : "";
                String telefono1 = result[7] != null ? (String) result[7] : "";
                String telefono2 = result[8] != null ? (String) result[8] : "";
                int tipoId = ((Number) result[9]).intValue();
                String argazkiaUrl = result[10] != null ? (String) result[10] : "";
                
                // Crear respuesta con datos completos del perfil
                String perfilData = String.format(
                    "{\"id\":%d,\"email\":\"%s\",\"username\":\"%s\",\"nombre\":\"%s\"," +
                    "\"apellidos\":\"%s\",\"dni\":\"%s\",\"direccion\":\"%s\"," +
                    "\"telefono1\":\"%s\",\"telefono2\":\"%s\",\"tipoId\":%d," +
                    "\"tipoNombre\":\"profesor\",\"argazkiaUrl\":\"%s\"}",
                    id, email, username != null ? username : "", 
                    nombre != null ? nombre : "", apellidos != null ? apellidos : "",
                    dni, direccion, telefono1, telefono2, tipoId, argazkiaUrl
                );
                
                SocketLogger.info("Perfil obtenido exitosamente para usuario: " + email);
                return Response.success("Perfil obtenido", perfilData);
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleGetPerfil", e);
            e.printStackTrace();
            return Response.error("Error obteniendo perfil: " + e.getMessage());
        }
    }
}