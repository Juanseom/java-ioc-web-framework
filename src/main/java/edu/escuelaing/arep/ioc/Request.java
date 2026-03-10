package edu.escuelaing.arep.ioc;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String queryString;
    private final Map<String, String> queryParams;

    public Request(String method, String path, String queryString) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.queryParams = new HashMap<>();
        parseQueryString();
    }

    private void parseQueryString() {
        if (queryString == null || queryString.isEmpty()) {
            return;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                queryParams.put(keyValue[0], "");
            }
        }
    }

    public String getValue(String key) {
        return queryParams.getOrDefault(key, "");
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}

