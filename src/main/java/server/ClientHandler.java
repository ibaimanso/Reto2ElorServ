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

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientId;
    private BufferedReader reader;
    private PrintWriter writer;
    private RequestProcessor requestProcessor;
    private boolean connected = true;
    private String sessionToken = null;

    public ClientHandler(Socket socket, int clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.requestProcessor = new RequestProcessor();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            SocketLogger.info("[Cliente #" + clientId + "] Handler iniciado");
            sendWelcomeMessage();
            String jsonMessage;
            while (connected && (jsonMessage = reader.readLine()) != null) {
                if (!JsonUtil.isValidJson(jsonMessage)) {
                    SocketLogger.warning("[Cliente #" + clientId + "] JSON inválido recibido");
                    sendResponse(Response.badRequest("JSON malformado"));
                    continue;
                }
                Request request = JsonUtil.fromJson(jsonMessage, Request.class);
                if (request == null || request.getAction() == null) {
                    SocketLogger.warning("[Cliente #" + clientId + "] Request inválido");
                    sendResponse(Response.badRequest("Petición inválida"));
                    continue;
                }
                SocketLogger.info("[Cliente #" + clientId + "] \u2192 " + request.getAction());
                Response response = requestProcessor.processRequest(request);
                if (request.getAction().toString().equals("LOGIN") && response.getStatus().getCode() == 200) {
                    sessionToken = request.getSessionToken();
                }
                sendResponse(response);
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

    private void sendWelcomeMessage() {
        Response welcome = Response.success("Conectado a ElorServ", null);
        sendResponse(welcome);
    }

    private void sendResponse(Response response) {
        String jsonResponse = JsonUtil.toJson(response);
        if (jsonResponse != null) {
            writer.println(jsonResponse);
            SocketLogger.debug("[Cliente #" + clientId + "] \u2190 " + response.getStatus());
        } else {
         //  SocketLogger.error("[Cliente #" + clientId + "] Error al serializar respuesta");
        }
    }

    private void closeConnection() {
        try {
            if (sessionToken != null) {
                SessionManager.invalidateSession(sessionToken);
            }
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
