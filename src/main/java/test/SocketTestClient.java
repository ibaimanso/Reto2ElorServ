package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import protocol.ActionType;
import protocol.Request;
import protocol.Response;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Cliente de prueba para testear el servidor de sockets.
 * Permite enviar diferentes tipos de peticiones y ver las respuestas.
 */
public class SocketTestClient {
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9000;
    
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gson;
    private String sessionToken;
    
    public SocketTestClient() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
    
    /**
     * Conectar al servidor
     */
    public boolean connect() {
        try {
            System.out.println("=== CONECTANDO AL SERVIDOR ===");
            System.out.println("Host: " + SERVER_HOST);
            System.out.println("Puerto: " + SERVER_PORT);
            
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("✓ Conectado exitosamente\n");
            return true;
            
        } catch (IOException e) {
            System.err.println("✗ Error al conectar: " + e.getMessage());
            System.err.println("\nAsegúrate de que el servidor está ejecutándose.");
            return false;
        }
    }
    
    /**
     * Enviar una petición y recibir respuesta
     */
    public Response sendRequest(Request request) {
        try {
            // Serializar request a JSON
            String jsonRequest = gson.toJson(request);
            
            System.out.println("→ Enviando petición:");
            System.out.println(gson.toJson(request));
            System.out.println();
            
            // Enviar al servidor
            output.println(jsonRequest);
            
            // Recibir respuesta
            String jsonResponse = input.readLine();
            
            if (jsonResponse != null) {
                Response response = gson.fromJson(jsonResponse, Response.class);
                
                System.out.println("← Respuesta recibida:");
                System.out.println(gson.toJson(response));
                System.out.println();
                
                return response;
            } else {
                System.err.println("✗ El servidor cerró la conexión");
                return null;
            }
            
        } catch (IOException e) {
            System.err.println("✗ Error en la comunicación: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cerrar conexión
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("✓ Desconectado del servidor");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    /**
     * Prueba de PING
     */
    public void testPing() {
        System.out.println("\n=== TEST: PING ===");
        Request request = new Request(ActionType.PING, null);
        sendRequest(request);
    }
    
    /**
     * Prueba de LOGIN
     */
    public void testLogin(String username, String password) {
        System.out.println("\n=== TEST: LOGIN ===");
        
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        
        Request request = new Request(ActionType.LOGIN, data);
        Response response = sendRequest(request);
        
        // Guardar token de sesión si el login fue exitoso
        if (response != null && response.getStatus().getCode() == 200) {
            // El token debería venir en response.getData()
            if (response.getData() != null) {
                Map<String, Object> responseData = (Map<String, Object>) response.getData();
                sessionToken = (String) responseData.get("sessionToken");
                System.out.println("✓ Session token guardado: " + sessionToken);
            }
        }
    }
    
    /**
     * Prueba de GET_PROFILE
     */
    public void testGetProfile() {
        System.out.println("\n=== TEST: GET_PROFILE ===");
        
        if (sessionToken == null) {
            System.err.println("✗ Debes hacer login primero");
            return;
        }
        
        Request request = new Request(ActionType.GET_PROFILE, sessionToken, null);
        sendRequest(request);
    }
    
    /**
     * Prueba de GET_MY_HORARIO
     */
    public void testGetMyHorario() {
        System.out.println("\n=== TEST: GET_MY_HORARIO ===");
        
        if (sessionToken == null) {
            System.err.println("✗ Debes hacer login primero");
            return;
        }
        
        Request request = new Request(ActionType.GET_MY_HORARIO, sessionToken, null);
        sendRequest(request);
    }
    
    /**
     * Prueba de LOGOUT
     */
    public void testLogout() {
        System.out.println("\n=== TEST: LOGOUT ===");
        
        if (sessionToken == null) {
            System.err.println("✗ No hay sesión activa");
            return;
        }
        
        Request request = new Request(ActionType.LOGOUT, sessionToken, null);
        sendRequest(request);
        sessionToken = null;
    }
    
    /**
     * Menú interactivo
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║    MENÚ DE PRUEBAS - ELORSERV     ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("1. PING - Probar conexión");
            System.out.println("2. LOGIN - Iniciar sesión");
            System.out.println("3. GET_PROFILE - Obtener perfil");
            System.out.println("4. GET_MY_HORARIO - Ver mi horario");
            System.out.println("5. LOGOUT - Cerrar sesión");
            System.out.println("0. Salir");
            System.out.println("────────────────────────────────────");
            System.out.print("Selecciona una opción: ");
            
            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (option) {
                    case 1:
                        testPing();
                        break;
                        
                    case 2:
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        testLogin(username, password);
                        break;
                        
                    case 3:
                        testGetProfile();
                        break;
                        
                    case 4:
                        testGetMyHorario();
                        break;
                        
                    case 5:
                        testLogout();
                        break;
                        
                    case 0:
                        running = false;
                        break;
                        
                    default:
                        System.out.println("Opción no válida");
                }
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Limpiar buffer
            }
        }
        
        scanner.close();
    }
    
    /**
     * Main - Punto de entrada
     */
    public static void main(String[] args) {
        SocketTestClient client = new SocketTestClient();
        
        // Conectar al servidor
        if (client.connect()) {
            // Mostrar menú interactivo
            client.showMenu();
            
            // Desconectar al salir
            client.disconnect();
        }
        
        System.out.println("\n¡Hasta luego!");
    }
}
