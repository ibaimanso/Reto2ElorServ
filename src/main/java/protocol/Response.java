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

    public StatusCode getStatus() { return status; }
    public Object getData() { return data; }
    public String getMessage() { return message; }
}
