package server;

import util.SocketLogger;
import util.RSAEncryptionUtil;
import security.SessionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SocketServer {
    
    private static final int PORT = 9000;  
    private static final int MAX_THREADS = 50;  
    
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean running = false;
    private int clientCounter = 0;
    
  
    public SocketServer() {
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
    }
    
    public void start() {
        try {
            SocketLogger.info("=== INICIANDO ELORSERV - SERVIDOR DE SOCKETS TCP ===");
            RSAEncryptionUtil.initializeKeys();
            
            serverSocket = new ServerSocket(PORT);
            running = true;
            
            SocketLogger.info("✓ Servidor escuchando en puerto: " + PORT);
            SocketLogger.info("✓ Thread pool inicializado con " + MAX_THREADS + " hilos máximos");
            SocketLogger.info("✓ Esperando conexiones de clientes ElorES...\n");
            
            acceptConnections();
            
        } catch (IOException e) {
            SocketLogger.error("Error al iniciar el servidor", e);
        }
    }
    
    /**
     * Bucle que acepta conexiones entrantes.
     */
    private void acceptConnections() {
        while (running) {
            try {
                // Esperar conexión de cliente (bloqueante)
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                
                String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                SocketLogger.info("→ Nuevo cliente conectado [#" + clientCounter + "]: " + clientInfo);
                
                // Crear handler para el cliente y ejecutar en thread pool
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCounter);
                threadPool.execute(clientHandler);
                
            } catch (IOException e) {
                if (running) {
                    SocketLogger.error("Error al aceptar conexión de cliente", e);
                }
            }
        }
    }
    
    /**
     * Detiene el servidor de forma ordenada.
     */
    public void shutdown() {
        SocketLogger.info("\n=== DETENIENDO SERVIDOR ===");
        running = false;
        
        try {
            // Cerrar ServerSocket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                SocketLogger.info("✓ ServerSocket cerrado");
            }
            
            // Cerrar sesiones activas
            SessionManager.clearAllSessions();
            
            // Apagar thread pool
            threadPool.shutdown();
            SocketLogger.info("Esperando que terminen los hilos activos...");
            
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                SocketLogger.warning("Forzado cierre de hilos después de 10 segundos");
            }
            
            SocketLogger.info("✓ Servidor detenido correctamente");
            
        } catch (IOException | InterruptedException e) {
            SocketLogger.error("Error al detener el servidor", e);
            threadPool.shutdownNow();
        }
    }
    
    /**
     * Obtiene el estado del servidor.
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Obtiene el número de clientes que se han conectado.
     */
    public int getClientCounter() {
        return clientCounter;
    }
    
    /**
     * Método main para iniciar el servidor.
     */
    public static void main(String[] args) {
        final SocketServer server = new SocketServer();
        
        // Hook para detener el servidor al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));
        
        // Iniciar servidor
        server.start();
    }
}
