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

/**
 * Cliente de prueba automático - ejecuta una serie de tests predefinidos.
 */
public class AutomaticTestClient {
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9000;
    
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gson;
    private String sessionToken;
    private int testsPassed = 0;
    private int testsFailed = 0;
    
    public AutomaticTestClient() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
    
    /**
     * Conectar al servidor
     */
    public boolean connect() {
        try {
            System.out.println("🔌 Conectando a " + SERVER_HOST + ":" + SERVER_PORT + "...");
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("✓ Conectado exitosamente\n");
            return true;
        } catch (IOException e) {
            System.err.println("✗ Error al conectar: " + e.getMessage());
            System.err.println("  Asegúrate de que el servidor está ejecutándose en el puerto " + SERVER_PORT);
            return false;
        }
    }
    
    /**
     * Enviar una petición y recibir respuesta
     */
    public Response sendRequest(Request request) {
        try {
            String jsonRequest = gson.toJson(request);
            output.println(jsonRequest);
            
            String jsonResponse = input.readLine();
            if (jsonResponse != null) {
                return gson.fromJson(jsonResponse, Response.class);
            }
            return null;
        } catch (IOException e) {
            System.err.println("✗ Error en la comunicación: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Desconectar
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignorar
        }
    }
    
    /**
     * Ejecutar un test
     */
    private void runTest(String testName, Request request, boolean shouldSucceed) {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🧪 TEST: " + testName);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("📤 Request: " + request.getAction());
        
        Response response = sendRequest(request);
        
        if (response == null) {
            System.err.println("✗ FALLO: No se recibió respuesta");
            testsFailed++;
            return;
        }
        
        System.out.println("📥 Response Status: " + response.getStatus());
        System.out.println("   Message: " + response.getMessage());
        
        boolean success = response.getStatus().getCode() >= 200 && response.getStatus().getCode() < 300;
        
        if (shouldSucceed && success) {
            System.out.println("✓ PASÓ - Test exitoso");
            testsPassed++;
        } else if (!shouldSucceed && !success) {
            System.out.println("✓ PASÓ - Error esperado recibido correctamente");
            testsPassed++;
        } else {
            System.err.println("✗ FALLÓ - Resultado inesperado");
            testsFailed++;
        }
        
        if (response.getData() != null) {
            System.out.println("   Data: " + gson.toJson(response.getData()));
        }
        
        System.out.println();
    }
    
    /**
     * Suite de tests automáticos
     */
    public void runTestSuite() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║  SUITE DE PRUEBAS AUTOMÁTICAS - ELORSERV ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        // TEST 1: PING
        runTest("PING - Verificar que el servidor responde",
                new Request(ActionType.PING, null),
                true);
        
        // TEST 2: LOGIN con credenciales incorrectas (debería fallar)
        Map<String, Object> wrongCredentials = new HashMap<>();
        wrongCredentials.put("username", "usuarioInexistente");
        wrongCredentials.put("password", "passwordIncorrecto");
        runTest("LOGIN - Credenciales incorrectas (debe fallar)",
                new Request(ActionType.LOGIN, wrongCredentials),
                false);
        
        // TEST 3: GET_PROFILE sin autenticación (debería fallar)
        runTest("GET_PROFILE sin autenticación (debe fallar)",
                new Request(ActionType.GET_PROFILE, null, null),
                false);
        
        // TEST 4: LOGIN con credenciales correctas
        // Usamos las credenciales del usuario 'profesor1' que existe en la base de datos reto2bbdd
        Map<String, Object> validCredentials = new HashMap<>();
        validCredentials.put("username", "profesor1");  // Usuario: Roman Lopez
        validCredentials.put("password", "123456");
        
        Request loginRequest = new Request(ActionType.LOGIN, validCredentials);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🧪 TEST: LOGIN - Credenciales válidas");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📤 Request: " + loginRequest.getAction());
        
        Response loginResponse = sendRequest(loginRequest);
        
        if (loginResponse != null && loginResponse.getStatus().getCode() == 200) {
            System.out.println("📥 Response Status: " + loginResponse.getStatus());
            System.out.println("   Message: " + loginResponse.getMessage());
            
            // Extraer token de sesión
            if (loginResponse.getData() != null) {
                Map<String, Object> responseData = (Map<String, Object>) loginResponse.getData();
                sessionToken = (String) responseData.get("sessionToken");
                System.out.println("   Session Token: " + (sessionToken != null ? sessionToken.substring(0, 20) + "..." : "null"));
            }
            
            System.out.println("✓ PASÓ - Login exitoso");
            testsPassed++;
        } else {
            System.err.println("✗ FALLÓ - No se pudo hacer login");
            System.err.println("  NOTA: Verifica que las credenciales 'admin'/'admin' existan en tu base de datos");
            if (loginResponse != null) {
                System.err.println("  Response: " + loginResponse.getMessage());
            }
            testsFailed++;
        }
        System.out.println();
        
        // Si el login fue exitoso, continuar con más tests
        if (sessionToken != null) {
            // TEST 5: GET_PROFILE con autenticación
            runTest("GET_PROFILE con autenticación válida",
                    new Request(ActionType.GET_PROFILE, sessionToken, null),
                    true);
            
            // TEST 6: GET_MY_HORARIO
            runTest("GET_MY_HORARIO con autenticación válida",
                    new Request(ActionType.GET_MY_HORARIO, sessionToken, null),
                    true);
            
            // TEST 7: LOGOUT
            runTest("LOGOUT",
                    new Request(ActionType.LOGOUT, sessionToken, null),
                    true);
        }
        
        // Resumen de resultados
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║           RESUMEN DE RESULTADOS          ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println("✓ Tests pasados: " + testsPassed);
        System.out.println("✗ Tests fallados: " + testsFailed);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        int total = testsPassed + testsFailed;
        double percentage = total > 0 ? (testsPassed * 100.0 / total) : 0;
        System.out.println(String.format("Tasa de éxito: %.1f%%", percentage));
        System.out.println();
    }
    
    /**
     * Main
     */
    public static void main(String[] args) {
        AutomaticTestClient client = new AutomaticTestClient();
        
        if (client.connect()) {
            client.runTestSuite();
            client.disconnect();
            System.out.println("✓ Desconectado del servidor");
        }
        
        System.out.println("\n¡Pruebas completadas!");
    }
}