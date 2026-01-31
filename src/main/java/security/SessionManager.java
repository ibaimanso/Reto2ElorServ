package security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, String> sessions = new ConcurrentHashMap<>();

    public static void createSession(String token, String userId) { sessions.put(token, userId); }
    public static void invalidateSession(String token) { sessions.remove(token); }
    public static void clearAllSessions() { sessions.clear(); }
}
