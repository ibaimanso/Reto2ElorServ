package server;

import protocol.Request;
import protocol.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import util.HibernateUtil;
import util.SocketLogger;

public class RequestProcessor {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public Response processRequest(Request request) {
        try {
            switch (request.getAction()) {
                case LOGIN:
                    return handleLogin(request);
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
            SocketLogger.info("Password recibida: " + password);
            
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
                    return Response.unauthorized("Usuario no encontrado");
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
                
                // SIMPLIFICADO: Solo verificar que el email existe y es profesor
                // No validamos contraseña por ahora
                
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
                SocketLogger.info("Datos a enviar: " + userData);
                
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
}