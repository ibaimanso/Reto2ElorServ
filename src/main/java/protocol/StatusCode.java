package protocol;

public class StatusCode {
    private int code;
    private String message;
    public StatusCode() {}
    public StatusCode(int code, String message) { this.code = code; this.message = message; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
}
