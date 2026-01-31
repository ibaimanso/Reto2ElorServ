package server;

import protocol.Request;
import protocol.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.SocketLogger;
import security.PasswordUtil;
import menu.servicios.EmailService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestProcessor {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public Response processRequest(Request request) {
        try {
            switch (request.getAction()) {
                case LOGIN:
                    return handleLogin(request);
                case GET_PERFIL:
                    return handleGetPerfil(request);
                case GET_ALUMNOS:
                    return handleGetAlumnos(request);
                case GET_PROFESORES:
                    return handleGetProfesores(request);
                case GET_HORARIO:
                    return handleGetHorario(request);
                case GET_REUNIONES:
                    return handleGetReuniones(request);
                case CREATE_REUNION:
                    return handleCreateReunion(request);
                case UPDATE_REUNION:
                    return handleUpdateReunion(request);
                case DELETE_REUNION:
                    return handleDeleteReunion(request);
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
    
    private Response handleGetAlumnos(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO GET_ALUMNOS ===");
            
            // Parsear payload con Jackson
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("profesorId")) {
                SocketLogger.warning("Falta campo profesorId");
                return Response.badRequest("profesorId es requerido");
            }
            
            int profesorId = payload.get("profesorId").asInt();
            SocketLogger.info("Obteniendo alumnos para el profesor ID: " + profesorId);
            
            // Buscar alumnos en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query compleja para obtener alumnos basándose en los módulos que imparte el profesor
                // La lógica es: obtener alumnos matriculados en los mismos ciclos y cursos
                // de los módulos que imparte el profesor
                java.util.List<Object[]> results = session.createNativeQuery(
                    "SELECT DISTINCT u.id, u.nombre, u.apellidos, u.email, u.dni, " +
                    "u.telefono1, u.telefono2, u.direccion, u.argazkia_url, " +
                    "c.nombre as ciclo, m.curso " +
                    "FROM users u " +
                    "INNER JOIN matriculaciones m ON u.id = m.alum_id " +
                    "INNER JOIN ciclos c ON m.ciclo_id = c.id " +
                    "WHERE u.tipo_id = 4 " +
                    "AND EXISTS ( " +
                    "    SELECT 1 FROM horarios h " +
                    "    INNER JOIN modulos modulo ON h.modulo_id = modulo.id " +
                    "    WHERE h.profe_id = :profesorId " +
                    "    AND modulo.ciclo_id = m.ciclo_id " +
                    "    AND modulo.curso_id = m.curso " +
                    ") " +
                    "ORDER BY u.apellidos, u.nombre")
                    .setParameter("profesorId", profesorId)
                    .getResultList();
                
                SocketLogger.info("Se encontraron " + results.size() + " alumnos para el profesor ID: " + profesorId);
                
                // Verificar si se encontraron alumnos
                if (results.isEmpty()) {
                    SocketLogger.warning("No se encontraron alumnos para el profesor ID: " + profesorId);
                    return Response.success("No hay alumnos registrados para este profesor", "[]");
                }
                
                // Construir arreglo JSON con los datos de los alumnos
                StringBuilder alumnosData = new StringBuilder("[");
                boolean first = true;
                
                for (Object[] alumno : results) {
                    if (!first) {
                        alumnosData.append(",");
                    }
                    first = false;
                    
                    int id = ((Number) alumno[0]).intValue();
                    String nombre = alumno[1] != null ? (String) alumno[1] : "";
                    String apellidos = alumno[2] != null ? (String) alumno[2] : "";
                    String email = alumno[3] != null ? (String) alumno[3] : "";
                    String dni = alumno[4] != null ? (String) alumno[4] : "";
                    String telefono1 = alumno[5] != null ? (String) alumno[5] : "";
                    String telefono2 = alumno[6] != null ? (String) alumno[6] : "";
                    String direccion = alumno[7] != null ? (String) alumno[7] : "";
                    String argazkiaUrl = alumno[8] != null ? (String) alumno[8] : "";
                    String ciclo = alumno[9] != null ? (String) alumno[9] : "";
                    int curso = alumno[10] != null ? ((Number) alumno[10]).intValue() : 0;
                    
                    // Escapar comillas en strings para JSON válido
                    nombre = nombre.replace("\"", "\\\"");
                    apellidos = apellidos.replace("\"", "\\\"");
                    email = email.replace("\"", "\\\"");
                    
                    String alumnoJson = String.format(
                        "{\"id\":%d,\"nombre\":\"%s\",\"apellidos\":\"%s\",\"email\":\"%s\"," +
                        "\"dni\":\"%s\",\"telefono1\":\"%s\",\"telefono2\":\"%s\"," +
                        "\"direccion\":\"%s\",\"argazkiaUrl\":\"%s\",\"ciclo\":\"%s\",\"curso\":\"%dº\"}",
                        id, nombre, apellidos, email, dni, telefono1, telefono2, 
                        direccion, argazkiaUrl, ciclo, curso
                    );
                    
                    alumnosData.append(alumnoJson);
                }
                
                alumnosData.append("]");
                
                SocketLogger.info("Alumnos obtenidos exitosamente: " + results.size() + " alumnos");
                return Response.success("Alumnos obtenidos", alumnosData.toString());
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleGetAlumnos", e);
            e.printStackTrace();
            return Response.error("Error obteniendo alumnos: " + e.getMessage());
        }
    }
    
    private Response handleGetProfesores(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO GET_PROFESORES ===");
            
            // No requiere payload específico, devuelve todos los profesores
            SocketLogger.info("Obteniendo lista de todos los profesores");
            
            // Buscar profesores en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query para obtener todos los usuarios con tipo_id = 3 (profesores)
                java.util.List<Object[]> results = session.createNativeQuery(
                    "SELECT u.id, u.nombre, u.apellidos, u.email " +
                    "FROM users u " +
                    "WHERE u.tipo_id = 3 " +
                    "ORDER BY u.apellidos, u.nombre")
                    .getResultList();
                
                SocketLogger.info("Se encontraron " + results.size() + " profesores en el sistema");
                
                // Verificar si se encontraron profesores
                if (results.isEmpty()) {
                    SocketLogger.warning("No se encontraron profesores en el sistema");
                    return Response.success("No hay profesores registrados", "[]");
                }
                
                // Construir arreglo JSON con los datos de los profesores
                StringBuilder profesoresData = new StringBuilder("[");
                boolean first = true;
                
                for (Object[] profesor : results) {
                    if (!first) {
                        profesoresData.append(",");
                    }
                    first = false;
                    
                    int id = ((Number) profesor[0]).intValue();
                    String nombre = profesor[1] != null ? (String) profesor[1] : "";
                    String apellidos = profesor[2] != null ? (String) profesor[2] : "";
                    String email = profesor[3] != null ? (String) profesor[3] : "";
                    
                    // Escapar comillas en strings para JSON válido
                    nombre = nombre.replace("\"", "\\\"");
                    apellidos = apellidos.replace("\"", "\\\"");
                    email = email.replace("\"", "\\\"");
                    
                    String profesorJson = String.format(
                        "{\"id\":%d,\"nombre\":\"%s\",\"apellidos\":\"%s\",\"email\":\"%s\"}",
                        id, nombre, apellidos, email
                    );
                    
                    profesoresData.append(profesorJson);
                }
                
                profesoresData.append("]");
                
                SocketLogger.info("Profesores obtenidos exitosamente: " + results.size() + " profesores");
                return Response.success("Profesores obtenidos", profesoresData.toString());
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD para profesores", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleGetProfesores", e);
            e.printStackTrace();
            return Response.error("Error obteniendo profesores: " + e.getMessage());
        }
    }
    
    private Response handleGetHorario(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO GET_HORARIO ===");
            
            // Parsear payload con Jackson
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("profesorId")) {
                SocketLogger.warning("Falta campo profesorId");
                return Response.badRequest("profesorId es requerido");
            }
            
            int profesorId = payload.get("profesorId").asInt();
            SocketLogger.info("Obteniendo horario para el profesor ID: " + profesorId);
            
            // Buscar horario en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query con JOINs para obtener el horario completo con nombres de módulos y ciclos
                java.util.List<Object[]> results = session.createNativeQuery(
                    "SELECT h.id, h.dia, h.hora, h.profe_id, h.modulo_id, h.aula, " +
                    "h.observaciones, h.ciclo_id, h.curso_id, " +
                    "m.nombre as modulo_nombre, " +
                    "c.nombre as ciclo_nombre " +
                    "FROM horarios h " +
                    "LEFT JOIN modulos m ON h.modulo_id = m.id " +
                    "LEFT JOIN ciclos c ON h.ciclo_id = c.id " +
                    "WHERE h.profe_id = :profesorId " +
                    "ORDER BY FIELD(h.dia, 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'), h.hora")
                    .setParameter("profesorId", profesorId)
                    .getResultList();
                
                SocketLogger.info("Se encontraron " + results.size() + " entradas de horario para el profesor ID: " + profesorId);
                
                // Verificar si se encontró horario
                if (results.isEmpty()) {
                    SocketLogger.warning("No se encontró horario para el profesor ID: " + profesorId);
                    return Response.error("No hay horario disponible para este profesor");
                }
                
                // Construir arreglo JSON con los datos del horario
                StringBuilder horarioData = new StringBuilder("[");
                boolean first = true;
                
                for (Object[] horario : results) {
                    if (!first) {
                        horarioData.append(",");
                    }
                    first = false;
                    
                    int id = ((Number) horario[0]).intValue();
                    String dia = (String) horario[1];
                    int hora = ((Number) horario[2]).intValue();
                    int profeId = ((Number) horario[3]).intValue();
                    int moduloId = ((Number) horario[4]).intValue();
                    String aula = horario[5] != null ? (String) horario[5] : "";
                    String observaciones = horario[6] != null ? (String) horario[6] : "";
                    String cicloId = horario[7] != null ? horario[7].toString() : "null";
                    String curso = horario[8] != null ? horario[8].toString() : "null";
                    String moduloNombre = horario[9] != null ? (String) horario[9] : "";
                    String cicloNombre = horario[10] != null ? (String) horario[10] : "";
                    
                    // Escapar comillas para JSON válido
                    aula = aula.replace("\"", "\\\"");
                    observaciones = observaciones.replace("\"", "\\\"");
                    moduloNombre = moduloNombre.replace("\"", "\\\"");
                    cicloNombre = cicloNombre.replace("\"", "\\\"");
                    
                    String horarioJson = String.format(
                        "{\"id\":%d,\"dia\":\"%s\",\"hora\":%d,\"profe_id\":%d," +
                        "\"modulo_id\":%d,\"aula\":\"%s\",\"observaciones\":\"%s\"," +
                        "\"ciclo_id\":%s,\"curso\":%s,\"modulo_nombre\":\"%s\",\"ciclo_nombre\":\"%s\"}",
                        id, dia, hora, profeId, moduloId, aula, observaciones, 
                        cicloId, curso, moduloNombre, cicloNombre
                    );
                    
                    horarioData.append(horarioJson);
                }
                
                horarioData.append("]");
                
                SocketLogger.info("Horario obtenido exitosamente: " + results.size() + " entradas");
                return Response.success("Horario obtenido correctamente", horarioData.toString());
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD para horario", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleGetHorario", e);
            e.printStackTrace();
            return Response.error("Error obteniendo horario: " + e.getMessage());
        }
    }
    
    private Response handleGetReuniones(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO GET_REUNIONES ===");
            
            // Parsear payload con Jackson
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("profesorId")) {
                SocketLogger.warning("Falta campo profesorId");
                return Response.badRequest("profesorId es requerido");
            }
            
            int profesorId = payload.get("profesorId").asInt();
            SocketLogger.info("Obteniendo reuniones para el profesor ID: " + profesorId);
            
            // Buscar reuniones en BD
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            try {
                // Query para obtener reuniones con cálculo de día y hora
                java.util.List<Object[]> results = session.createNativeQuery(
                    "SELECT r.id_reunion, r.estado, r.profesor_id, r.alumno_id, " +
                    "r.titulo, r.asunto, r.aula, r.fecha, " +
                    "DAYNAME(r.fecha) as dia_semana, " +
                    "HOUR(r.fecha) as hora_reunion " +
                    "FROM reuniones r " +
                    "WHERE r.profesor_id = :profesorId " +
                    "ORDER BY r.fecha")
                    .setParameter("profesorId", profesorId)
                    .getResultList();
                
                SocketLogger.info("Se encontraron " + results.size() + " reuniones para el profesor ID: " + profesorId);
                
                // Si no hay reuniones, retornar array vacío (no es error)
                if (results.isEmpty()) {
                    SocketLogger.info("No hay reuniones para el profesor ID: " + profesorId);
                    return Response.success("No hay reuniones programadas", "[]");
                }
                
                // Construir arreglo JSON con los datos de las reuniones
                StringBuilder reunionesData = new StringBuilder("[");
                boolean first = true;
                
                for (Object[] reunion : results) {
                    if (!first) {
                        reunionesData.append(",");
                    }
                    first = false;
                    
                    int idReunion = ((Number) reunion[0]).intValue();
                    String estado = reunion[1] != null ? (String) reunion[1] : "pendiente";
                    String profesorIdStr = reunion[2] != null ? reunion[2].toString() : "null";
                    String alumnoIdStr = reunion[3] != null ? reunion[3].toString() : "null";
                    String titulo = reunion[4] != null ? (String) reunion[4] : "";
                    String asunto = reunion[5] != null ? (String) reunion[5] : "";
                    String aula = reunion[6] != null ? (String) reunion[6] : "";
                    // fecha en reunion[7]
                    String diaSemana = reunion[8] != null ? (String) reunion[8] : "";
                    String horaStr = reunion[9] != null ? reunion[9].toString() : "0";
                    
                    // Mapear DAYNAME inglés a español
                    String dia = mapearDiaSemana(diaSemana);
                    
                    // Escapar comillas para JSON válido
                    titulo = titulo.replace("\"", "\\\"");
                    asunto = asunto.replace("\"", "\\\"");
                    aula = aula.replace("\"", "\\\"");
                    
                    String reunionJson = String.format(
                        "{\"id_reunion\":%d,\"estado\":\"%s\",\"profesor_id\":%s," +
                        "\"alumno_id\":%s,\"titulo\":\"%s\",\"asunto\":\"%s\"," +
                        "\"aula\":\"%s\",\"dia\":\"%s\",\"hora\":%s}",
                        idReunion, estado, profesorIdStr, alumnoIdStr, 
                        titulo, asunto, aula, dia, horaStr
                    );
                    
                    reunionesData.append(reunionJson);
                }
                
                reunionesData.append("]");
                
                SocketLogger.info("Reuniones obtenidas exitosamente: " + results.size() + " reuniones");
                return Response.success("Reuniones obtenidas correctamente", reunionesData.toString());
                
            } catch (Exception e) {
                SocketLogger.error("Error en consulta BD para reuniones", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleGetReuniones", e);
            e.printStackTrace();
            return Response.error("Error obteniendo reuniones: " + e.getMessage());
        }
    }
    
    private Response handleCreateReunion(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO CREATE_REUNION ===");
            
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            // Validar campos requeridos
            if (!payload.has("profesorId") || !payload.has("alumnoId") || 
                !payload.has("titulo") || !payload.has("fecha")) {
                return Response.badRequest("Faltan campos requeridos para crear reunión");
            }
            
            int profesorId = payload.get("profesorId").asInt();
            int alumnoId = payload.get("alumnoId").asInt();
            String titulo = payload.get("titulo").asText();
            String asunto = payload.has("asunto") ? payload.get("asunto").asText() : "";
            String aula = payload.has("aula") ? payload.get("aula").asText() : "";
            String fechaStr = payload.get("fecha").asText();
            
            SocketLogger.info("Creando reunión: " + titulo + " para alumno " + alumnoId);
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            
            try {
                tx = session.beginTransaction();
                
                // Parsear fecha
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                
                // No verificar conflictos - guardar siempre como pendiente
                String estadoInicial = "pendiente";
                
                SocketLogger.info("Guardando reunión con estado: " + estadoInicial);
                
                // Insertar reunión en la BD
                session.createNativeQuery(
                    "INSERT INTO reuniones (estado, profesor_id, alumno_id, titulo, asunto, aula, fecha, id_centro) " +
                    "VALUES (:estado, :profesorId, :alumnoId, :titulo, :asunto, :aula, :fecha, '15112')")
                    .setParameter("estado", estadoInicial)
                    .setParameter("profesorId", profesorId)
                    .setParameter("alumnoId", alumnoId)
                    .setParameter("titulo", titulo)
                    .setParameter("asunto", asunto)
                    .setParameter("aula", aula)
                    .setParameter("fecha", fecha)
                    .executeUpdate();
                
                tx.commit();
                
                SocketLogger.info("✅ Reunión creada exitosamente con estado: " + estadoInicial);
                
                // Enviar correos electrónicos
                try {
                    EmailService emailService = EmailServiceHolder.getEmailService();
                    if (emailService != null) {
                        // Obtener emails del profesor y alumno
                        Object[] profesor = (Object[]) session.createNativeQuery(
                            "SELECT nombre, apellidos, email FROM users WHERE id = :id")
                            .setParameter("id", profesorId)
                            .uniqueResult();
                        
                        Object[] alumno = (Object[]) session.createNativeQuery(
                            "SELECT email FROM users WHERE id = :id")
                            .setParameter("id", alumnoId)
                            .uniqueResult();
                        
                        if (profesor != null && alumno != null) {
                            String nombreProfesor = profesor[0] + " " + profesor[1];
                            String emailAlumno = (String) alumno[0];
                            String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            String horaFormato = fecha.format(DateTimeFormatter.ofPattern("HH:mm"));
                            
                            try {
                                emailService.sendReunionCreada(emailAlumno, nombreProfesor, 
                                                             titulo, fechaFormato, horaFormato, aula);
                                SocketLogger.info("📧 Correo enviado a alumno: " + emailAlumno);
                            } catch (Exception emailEx) {
                                SocketLogger.error("Error al enviar correo a alumno: " + emailEx.getMessage(), emailEx);
                            }
                        } else {
                            SocketLogger.warning("No se encontraron datos del profesor o alumno para enviar correo");
                        }
                    } else {
                        SocketLogger.warning("⚠️ EmailService no disponible - no se envió correo. Verifica la configuración de Spring Mail en application.properties");
                    }
                } catch (Exception e) {
                    SocketLogger.error("Error en el proceso de envío de correo: " + e.getMessage(), e);
                    // No fallar la operación si el correo falla
                }
                
                return Response.success("Reunión creada exitosamente", 
                    "{\"estado\":\"" + estadoInicial + "\"}");
                
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                SocketLogger.error("Error creando reunión", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleCreateReunion", e);
            return Response.error("Error creando reunión: " + e.getMessage());
        }
    }
    
    private Response handleUpdateReunion(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO UPDATE_REUNION ===");
            
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("reunionId") || !payload.has("estado")) {
                return Response.badRequest("Faltan campos requeridos para actualizar reunión");
            }
            
            int reunionId = payload.get("reunionId").asInt();
            String nuevoEstado = payload.get("estado").asText();
            
            SocketLogger.info("Actualizando reunión " + reunionId + " a estado: " + nuevoEstado);
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            
            try {
                tx = session.beginTransaction();
                
                // Obtener información de la reunión antes de actualizar
                Object[] reunion = (Object[]) session.createNativeQuery(
                    "SELECT titulo, fecha, profesor_id, alumno_id FROM reuniones " +
                    "WHERE id_reunion = :id")
                    .setParameter("id", reunionId)
                    .uniqueResult();
                
                if (reunion == null) {
                    return Response.error("Reunión no encontrada");
                }
                
                String titulo = (String) reunion[0];
                
                // Manejar conversión de fecha de forma segura
                LocalDateTime fecha;
                Object fechaObj = reunion[1];
                if (fechaObj instanceof java.sql.Timestamp) {
                    fecha = ((java.sql.Timestamp) fechaObj).toLocalDateTime();
                } else if (fechaObj instanceof LocalDateTime) {
                    fecha = (LocalDateTime) fechaObj;
                } else {
                    SocketLogger.warning("Tipo de fecha inesperado: " + fechaObj.getClass().getName());
                    return Response.error("Error procesando fecha de reunión");
                }
                
                int profesorId = ((Number) reunion[2]).intValue();
                int alumnoId = ((Number) reunion[3]).intValue();
                
                // Actualizar estado
                int updated = session.createNativeQuery(
                    "UPDATE reuniones SET estado = :estado WHERE id_reunion = :id")
                    .setParameter("estado", nuevoEstado)
                    .setParameter("id", reunionId)
                    .executeUpdate();
                
                if (updated == 0) {
                    return Response.error("No se pudo actualizar la reunión");
                }
                
                tx.commit();
                
                SocketLogger.info("✅ Estado de reunión actualizado");
                
                // Enviar correos
                try {
                    EmailService emailService = EmailServiceHolder.getEmailService();
                    if (emailService != null) {
                        // Obtener emails
                        String emailAlumno = (String) session.createNativeQuery(
                            "SELECT email FROM users WHERE id = :id")
                            .setParameter("id", alumnoId)
                            .uniqueResult();
                        
                        String emailProfesor = (String) session.createNativeQuery(
                            "SELECT email FROM users WHERE id = :id")
                            .setParameter("id", profesorId)
                            .uniqueResult();
                        
                        String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        String horaFormato = fecha.format(DateTimeFormatter.ofPattern("HH:mm"));
                        
                        if (emailAlumno != null) {
                            emailService.sendReunionEstadoCambiado(emailAlumno, titulo, 
                                                                 nuevoEstado, fechaFormato, horaFormato);
                        }
                        if (emailProfesor != null) {
                            emailService.sendReunionEstadoCambiado(emailProfesor, titulo, 
                                                                 nuevoEstado, fechaFormato, horaFormato);
                        }
                        
                        SocketLogger.info("📧 Correos de actualización enviados");
                    }
                } catch (Exception e) {
                    SocketLogger.warning("Error enviando correos: " + e.getMessage());
                }
                
                return Response.success("Estado actualizado correctamente", null);
                
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                SocketLogger.error("Error actualizando reunión", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleUpdateReunion", e);
            return Response.error("Error actualizando reunión: " + e.getMessage());
        }
    }
    
    private Response handleDeleteReunion(Request request) {
        try {
            SocketLogger.info("=== PROCESANDO DELETE_REUNION ===");
            
            JsonNode payload = objectMapper.readTree(request.getPayload());
            
            if (!payload.has("reunionId")) {
                return Response.badRequest("Falta reunionId");
            }
            
            int reunionId = payload.get("reunionId").asInt();
            
            SocketLogger.info("Eliminando reunión: " + reunionId);
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            
            try {
                tx = session.beginTransaction();
                
                // Obtener información antes de eliminar
                Object[] reunion = (Object[]) session.createNativeQuery(
                    "SELECT titulo, fecha, profesor_id, alumno_id FROM reuniones " +
                    "WHERE id_reunion = :id")
                    .setParameter("id", reunionId)
                    .uniqueResult();
                
                if (reunion == null) {
                    return Response.error("Reunión no encontrada");
                }
                
                String titulo = (String) reunion[0];
                
                // Manejar conversión de fecha de forma segura
                LocalDateTime fecha;
                Object fechaObj = reunion[1];
                if (fechaObj instanceof java.sql.Timestamp) {
                    fecha = ((java.sql.Timestamp) fechaObj).toLocalDateTime();
                } else if (fechaObj instanceof LocalDateTime) {
                    fecha = (LocalDateTime) fechaObj;
                } else {
                    SocketLogger.warning("Tipo de fecha inesperado: " + fechaObj.getClass().getName());
                    return Response.error("Error procesando fecha de reunión");
                }
                
                int profesorId = ((Number) reunion[2]).intValue();
                int alumnoId = ((Number) reunion[3]).intValue();
                
                // Eliminar reunión
                int deleted = session.createNativeQuery(
                    "DELETE FROM reuniones WHERE id_reunion = :id")
                    .setParameter("id", reunionId)
                    .executeUpdate();
                
                if (deleted == 0) {
                    return Response.error("No se pudo eliminar la reunión");
                }
                
                tx.commit();
                
                SocketLogger.info("✅ Reunión eliminada");
                
                // Enviar correos
                try {
                    EmailService emailService = EmailServiceHolder.getEmailService();
                    if (emailService != null) {
                        String emailAlumno = (String) session.createNativeQuery(
                            "SELECT email FROM users WHERE id = :id")
                            .setParameter("id", alumnoId)
                            .uniqueResult();
                        
                        String emailProfesor = (String) session.createNativeQuery(
                            "SELECT email FROM users WHERE id = :id")
                            .setParameter("id", profesorId)
                            .uniqueResult();
                        
                        String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        String horaFormato = fecha.format(DateTimeFormatter.ofPattern("HH:mm"));
                        
                        if (emailAlumno != null) {
                            emailService.sendReunionEliminada(emailAlumno, titulo, fechaFormato, horaFormato);
                        }
                        if (emailProfesor != null) {
                            emailService.sendReunionEliminada(emailProfesor, titulo, fechaFormato, horaFormato);
                        }
                        
                        SocketLogger.info("📧 Correos de cancelación enviados");
                    }
                } catch (Exception e) {
                    SocketLogger.warning("Error enviando correos: " + e.getMessage());
                }
                
                return Response.success("Reunión eliminada correctamente", null);
                
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                SocketLogger.error("Error eliminando reunión", e);
                throw e;
            } finally {
                session.close();
            }
            
        } catch (Exception e) {
            SocketLogger.error("Error en handleDeleteReunion", e);
            return Response.error("Error eliminando reunión: " + e.getMessage());
        }
    }
    
    private String obtenerDiaSemana(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "LUNES";
            case 2: return "MARTES";
            case 3: return "MIERCOLES";
            case 4: return "JUEVES";
            case 5: return "VIERNES";
            case 6: return "SABADO";
            case 7: return "DOMINGO";
            default: return "";
        }
    }
    
    /**
     * Mapea el nombre del día en inglés (DAYNAME) a español
     */
    private String mapearDiaSemana(String dayName) {
        if (dayName == null || dayName.isEmpty()) {
            return "";
        }
        
        switch (dayName.toUpperCase()) {
            case "MONDAY":
                return "LUNES";
            case "TUESDAY":
                return "MARTES";
            case "WEDNESDAY":
                return "MIERCOLES";
            case "THURSDAY":
                return "JUEVES";
            case "FRIDAY":
                return "VIERNES";
            case "SATURDAY":
                return "SABADO";
            case "SUNDAY":
                return "DOMINGO";
            default:
                return dayName;
        }
    }
}
