import server.SocketServer;
import util.HibernateUtil;
import util.SocketLogger;

/**
 * Clase principal para iniciar el servidor ElorServ.
 * 
 * Este Main:
 * 1. Verifica la conexión a la base de datos reto2bbdd
 * 2. Inicia el servidor de sockets en el puerto 9000
 * 3. Queda a la espera de conexiones de clientes
 * 
 * REQUISITOS PREVIOS:
 * - XAMPP ejecutándose con MySQL en puerto 3308
 * - Base de datos 'reto2bbdd' creada e importada
 * - Al menos un usuario en la tabla 'users' para hacer pruebas de login
 * 
 * @author ElorServ Team
 * @version 1.0
 */
public class MainServer {

    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ELORSERV - SERVIDOR TCP                    ║");
        System.out.println("║            Sistema de Gestión Educativa - Elorrieta          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Paso 1: Verificar conexión a la base de datos
        SocketLogger.info("📊 PASO 1: Verificando conexión a la base de datos...");
        if (!testDatabaseConnection()) {
            SocketLogger.error("✗ ERROR: No se pudo conectar a la base de datos");
            System.err.println("\n⚠️  VERIFICA:");
            System.err.println("   1. XAMPP está ejecutándose");
            System.err.println("   2. MySQL está activo en el puerto 3306");
            System.err.println("   3. La base de datos 'reto2bbdd' existe");
            System.err.println("   4. Las credenciales en hibernate.cfg.xml son correctas");
            System.err.println();
            return;
        }
        
        SocketLogger.info("✓ Conexión a base de datos exitosa");
        System.out.println();
        
        // Paso 2: Iniciar el servidor de sockets
        SocketLogger.info("🚀 PASO 2: Iniciando servidor de sockets...");
        SocketServer server = new SocketServer();
        
        // Agregar shutdown hook para cerrar limpiamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n\n🛑 Señal de apagado recibida...");
            server.shutdown();
            HibernateUtil.shutdown();
            System.out.println("✓ Servidor detenido correctamente");
        }));
        
        // Iniciar servidor (este método es bloqueante)
        server.start();
    }
    
    /**
     * Prueba la conexión a la base de datos intentando abrir una sesión de Hibernate.
     * 
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    private static boolean testDatabaseConnection() {
        try {
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            
            // Intentar una consulta simple para verificar que todo funciona
            Long userCount = (Long) session.createQuery("SELECT COUNT(u) FROM Users u").uniqueResult();
            
            SocketLogger.info("   → Base de datos: reto2bbdd");
            SocketLogger.info("   → Usuarios registrados: " + userCount);
            
            // Verificar que hay usuarios para poder hacer login
            if (userCount == 0) {
                System.err.println("\n⚠️  ADVERTENCIA: No hay usuarios en la base de datos");
                System.err.println("   No podrás hacer login. Importa el script SQL proporcionado.");
            }
            
            session.close();
            return true;
            
        } catch (Exception e) {
            SocketLogger.error("Error al conectar con la base de datos: " + e.getMessage(), e);
            return false;
        }
    }
}
