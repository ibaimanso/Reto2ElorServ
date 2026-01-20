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

            SocketLogger.info("\u2713 Servidor escuchando en puerto: " + PORT);
            SocketLogger.info("\u2713 Thread pool inicializado con " + MAX_THREADS + " hilos máximos");
            SocketLogger.info("\u2713 Esperando conexiones de clientes ElorES...\n");

            acceptConnections();
        } catch (IOException e) {
            SocketLogger.error("Error al iniciar el servidor", e);
        }
    }

    private void acceptConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                SocketLogger.info("\u2192 Nuevo cliente conectado [#" + clientCounter + "]: " + clientInfo);
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCounter);
                threadPool.execute(clientHandler);
            } catch (IOException e) {
                if (running) {
                    SocketLogger.error("Error al aceptar conexión de cliente", e);
                }
            }
        }
    }

    public void shutdown() {
        SocketLogger.info("\n=== DETENIENDO SERVIDOR ===");
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                SocketLogger.info("\u2713 ServerSocket cerrado");
            }
            SessionManager.clearAllSessions();
            threadPool.shutdown();
            SocketLogger.info("Esperando que terminen los hilos activos...");
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                SocketLogger.warning("Forzado cierre de hilos después de 10 segundos");
            }
            SocketLogger.info("\u2713 Servidor detenido correctamente");
        } catch (IOException | InterruptedException e) {
            SocketLogger.error("Error al detener el servidor", e);
            threadPool.shutdownNow();
        }
    }

    public boolean isRunning() { return running; }
    public int getClientCounter() { return clientCounter; }
}
