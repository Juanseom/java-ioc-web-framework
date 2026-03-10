package edu.escuelaing.arep.ioc;

public class Response {

    private int statusCode;
    private String body;

    public Response() {
        this.statusCode = 200;
        this.body = "";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

