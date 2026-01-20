package server;

import protocol.Request;
import protocol.Response;

public class RequestProcessor {
    public Response processRequest(Request request) {
        // Placeholder: replicate your business logic here
        // For now echo success with the same action
        return Response.success("Acción procesada: " + request.getAction(), null);
    }
}
