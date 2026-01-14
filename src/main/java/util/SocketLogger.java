package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger específico para el sistema de sockets.
 * Registra eventos en archivo y consola.
 */
public class SocketLogger {
    
    private static final String LOG_FILE = "logs/socket_server.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean logToFile = true;
    
    public enum LogLevel {
        INFO, WARNING, ERROR, DEBUG
    }
    
    /**
     * Registra un mensaje con nivel específico.
     */
    public static void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Log en consola
        System.out.println(logMessage);
        
        // Log en archivo
        if (logToFile) {
            writeToFile(logMessage);
        }
    }
    
    public static void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    public static void warning(String message) {
        log(LogLevel.WARNING, message);
    }
    
    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    public static void error(String message, Exception e) {
        error(message + " - " + e.getMessage());
        e.printStackTrace();
    }
    
    /**
     * Escribe en archivo de log.
     */
    private static void writeToFile(String message) {
        try {
            java.io.File logDir = new java.io.File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(message);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en archivo de log: " + e.getMessage());
        }
    }
    
    public static void setLogToFile(boolean enabled) {
        logToFile = enabled;
    }
}
