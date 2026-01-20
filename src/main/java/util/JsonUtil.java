package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean isValidJson(String json) {
        try { mapper.readTree(json); return true; } catch (Exception e) { return false; }
    }
    public static <T> T fromJson(String json, Class<T> cls) {
        try { return mapper.readValue(json, cls); } catch (Exception e) { return null; }
    }
    public static String toJson(Object obj) {
        try { return mapper.writeValueAsString(obj); } catch (JsonProcessingException e) { return null; }
    }
}
