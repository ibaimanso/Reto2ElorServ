package util;

public class SocketLogger {
    public static void info(String msg) { System.out.println(msg); }
    public static void warning(String msg) { System.out.println("[WARN] " + msg); }
    public static void error(String msg, Throwable t) { System.err.println(msg); if (t != null) t.printStackTrace(); }
    public static void debug(String msg) { System.out.println("[DEBUG] " + msg); }
}
