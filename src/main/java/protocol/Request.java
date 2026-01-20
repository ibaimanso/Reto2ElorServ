package protocol;

public class Request {
    private ActionType action;
    private String sessionToken;
    private String payload;
    public ActionType getAction() { return action; }
    public void setAction(ActionType action) { this.action = action; }
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
}
