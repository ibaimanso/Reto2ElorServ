package protocol;

public class Response {
    private StatusCode status;
    private Object data;
    private String message;

    public Response() {}
    public Response(StatusCode status, String message, Object data) {
        this.status = status; this.message = message; this.data = data;
    }

    public static Response success(String message, Object data) {
        return new Response(new StatusCode(200, "OK"), message, data);
    }
    
    public static Response badRequest(String message) {
        return new Response(new StatusCode(400, "Bad Request"), message, null);
    }
    
    public static Response unauthorized(String message) {
        return new Response(new StatusCode(401, "Unauthorized"), message, null);
    }
    
    public static Response forbidden(String message) {
        return new Response(new StatusCode(403, "Forbidden"), message, null);
    }
    
    public static Response error(String message) {
        return new Response(new StatusCode(500, "Internal Server Error"), message, null);
    }

    public StatusCode getStatus() { return status; }
    public Object getData() { return data; }
    public String getMessage() { return message; }
}