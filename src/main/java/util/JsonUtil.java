package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Utilidad para serializar y deserializar objetos a/desde JSON.
 * Usa Gson para la conversión.
 */
public class JsonUtil {
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();
    
    /**
     * Convierte un objeto a JSON String.
     */
    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            System.err.println("Error al serializar objeto a JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convierte un JSON String a un objeto de la clase especificada.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            System.err.println("Error al deserializar JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Valida si un String es un JSON válido.
     */
    public static boolean isValidJson(String json) {
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
