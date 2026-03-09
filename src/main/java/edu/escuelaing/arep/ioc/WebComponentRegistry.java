package edu.escuelaing.arep.ioc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WebComponentRegistry {

    private static final Map<String, RouteDefinition> routes = new HashMap<>();

    private WebComponentRegistry() {
    }

    public static void registerControllerClass(String controllerClassName) throws Exception {
        Class<?> controllerClass = Class.forName(controllerClassName);

        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("Class is not annotated with @RestController: " + controllerClassName);
        }

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                if (!method.getReturnType().equals(String.class)) {
                    throw new IllegalArgumentException("Only String return type is supported for @GetMapping methods: " + method.getName());
                }
                if (method.getParameterCount() > 0) {
                    throw new IllegalArgumentException("Parameters are not supported yet in this version: " + method.getName());
                }

                String path = method.getAnnotation(GetMapping.class).value();
                routes.put(path, new RouteDefinition(controllerInstance, method));
            }
        }
    }

    public static RouteDefinition getRoute(String path) {
        return routes.get(path);
    }

    public static class RouteDefinition {
        private final Object instance;
        private final Method method;

        public RouteDefinition(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }

        public Object getInstance() {
            return instance;
        }

        public Method getMethod() {
            return method;
        }
    }
}

