package server;

import protocol.Request;
import protocol.Response;
import util.JsonUtil;
import util.SocketLogger;
import security.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Gestiona la comunicación con un cliente conectado.
 * Cada cliente tiene su propio hilo (Runnable).
 */
public class ClientHandler implements Runnable {
    
    private Socket clientSocket;
    private int clientId;
    private BufferedReader reader;
    private PrintWriter writer;
    private RequestProcessor requestProcessor;
    private boolean connected = true;
    private String sessionToken = null;
    
    /**
     * Constructor del handler de cliente.
     */
    public ClientHandler(Socket socket, int clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.requestProcessor = new RequestProcessor();
    }
    
    @Override
    public void run() {
        try {
            // Inicializar streams de entrada/salida
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            
            SocketLogger.info("[Cliente #" + clientId + "] Handler iniciado");
            
            // Enviar mensaje de bienvenida
            sendWelcomeMessage();
            
            // Bucle de escucha de mensajes del cliente
            String jsonMessage;
            while (connected && (jsonMessage = reader.readLine()) != null) {
                
                // Validar JSON
                if (!JsonUtil.isValidJson(jsonMessage)) {
                    SocketLogger.warning("[Cliente #" + clientId + "] JSON inválido recibido");
                    sendResponse(Response.badRequest("JSON malformado"));
                    continue;
                }
                
                // Deserializar Request
                Request request = JsonUtil.fromJson(jsonMessage, Request.class);
                if (request == null || request.getAction() == null) {
                    SocketLogger.warning("[Cliente #" + clientId + "] Request inválido");
                    sendResponse(Response.badRequest("Petición inválida"));
                    continue;
                }
                
                SocketLogger.info("[Cliente #" + clientId + "] → " + request.getAction());
                
                // Procesar la petición
                Response response = requestProcessor.processRequest(request);
                
                // Si es un login exitoso, guardar el token
                if (request.getAction().toString().equals("LOGIN") && 
                    response.getStatus().getCode() == 200) {
                    // Extraer token de la respuesta (se asume que viene en data)
                    sessionToken = request.getSessionToken();
                }
                
                // Enviar respuesta al cliente
                sendResponse(response);
                
                // Si es DISCONNECT, cerrar conexión
                if (request.getAction().toString().equals("DISCONNECT")) {
                    connected = false;
                }
            }
            
        } catch (SocketException e) {
            SocketLogger.warning("[Cliente #" + clientId + "] Desconexión inesperada");
        } catch (IOException e) {
            SocketLogger.error("[Cliente #" + clientId + "] Error de I/O", e);
        } finally {
            closeConnection();
        }
    }
    
    /**
     * Envía un mensaje de bienvenida al cliente.
     */
    private void sendWelcomeMessage() {
        Response welcome = Response.success("Conectado a ElorServ", null);
        sendResponse(welcome);
    }
    
    /**
     * Envía una respuesta al cliente.
     */
    private void sendResponse(Response response) {
        String jsonResponse = JsonUtil.toJson(response);
        if (jsonResponse != null) {
            writer.println(jsonResponse);
            SocketLogger.debug("[Cliente #" + clientId + "] ← " + response.getStatus());
        } else {
            SocketLogger.error("[Cliente #" + clientId + "] Error al serializar respuesta");
        }
    }
    
    /**
     * Cierra la conexión con el cliente.
     */
    private void closeConnection() {
        try {
            // Invalidar sesión si existe
            if (sessionToken != null) {
                SessionManager.invalidateSession(sessionToken);
            }
            
            // Cerrar streams
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            
            SocketLogger.info("[Cliente #" + clientId + "] Desconectado");
            
        } catch (IOException e) {
            SocketLogger.error("[Cliente #" + clientId + "] Error al cerrar conexión", e);
        }
    }
}
