package edu.escuelaing.arep.ioc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {

    private static final int PORT = 8080;
    private static final String STATIC_FOLDER = "/webroot";

    private HttpServer() {
    }

    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("MicroSpringBoot running on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleRequest(clientSocket);
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            close(clientSocket, in, out);
            return;
        }

        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // Skip headers in this basic version.
        }

        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            writeTextResponse(out, 400, "Bad Request");
            close(clientSocket, in, out);
            return;
        }

        String method = parts[0];
        String fullPath = parts[1];

        String path = fullPath;
        String queryString = "";
        if (fullPath.contains("?")) {
            path = fullPath.substring(0, fullPath.indexOf("?"));
            queryString = fullPath.substring(fullPath.indexOf("?") + 1);
        }

        if ("GET".equals(method) && tryHandleComponentRoute(path, queryString, out)) {
            close(clientSocket, in, out);
            return;
        }

        if ("GET".equals(method)) {
            handleStaticFile(path, out);
        } else {
            writeTextResponse(out, 405, "Method Not Allowed");
        }

        close(clientSocket, in, out);
    }

    private static boolean tryHandleComponentRoute(String path, String queryString, OutputStream out) throws IOException {
        WebComponentRegistry.RouteDefinition route = WebComponentRegistry.getRoute(path);
        if (route == null) {
            return false;
        }

        try {
            Request req = new Request("GET", path, queryString);
            Response res = new Response();
            String body = (String) route.getMethod().invoke(route.getInstance());
            if (body == null) {
                body = "";
            }
            res.setBody(body);
            writeTextResponse(out, res.getStatusCode(), res.getBody());
        } catch (Exception e) {
            writeTextResponse(out, 500, "Internal Server Error");
        }
        return true;
    }

    private static void handleStaticFile(String path, OutputStream out) throws IOException {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        InputStream fileStream = HttpServer.class.getResourceAsStream(STATIC_FOLDER + path);
        if (fileStream == null) {
            writeTextResponse(out, 404, "404 Not Found");
            return;
        }

        byte[] fileBytes = fileStream.readAllBytes();
        fileStream.close();

        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + getContentType(path) + "\r\n"
                + "Content-Length: " + fileBytes.length + "\r\n"
                + "\r\n";

        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(fileBytes);
    }

    private static void writeTextResponse(OutputStream out, int statusCode, String body) throws IOException {
        String statusText = getStatusText(statusCode);
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Length: " + bodyBytes.length + "\r\n"
                + "\r\n";

        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
    }

    private static String getStatusText(int statusCode) {
        if (statusCode == 200) {
            return "OK";
        }
        if (statusCode == 400) {
            return "Bad Request";
        }
        if (statusCode == 404) {
            return "Not Found";
        }
        if (statusCode == 405) {
            return "Method Not Allowed";
        }
        return "Internal Server Error";
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html";
        }
        if (path.endsWith(".png")) {
            return "image/png";
        }
        return "text/plain";
    }

    private static void close(Socket clientSocket, BufferedReader in, OutputStream out) throws IOException {
        out.close();
        in.close();
        clientSocket.close();
    }
}

