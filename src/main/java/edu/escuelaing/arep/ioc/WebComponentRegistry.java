package edu.escuelaing.arep.ioc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebComponentRegistry {

    private static final Map<String, RouteDefinition> routes = new HashMap<>();

    private WebComponentRegistry() {
    }

    public static void registerControllerClass(String controllerClassName) throws Exception {
        Class<?> controllerClass = Class.forName(controllerClassName);
        registerControllerClass(controllerClass);
    }

    public static int registerControllersInPackage(String basePackage) throws Exception {
        List<String> classNames = ClassPathScanner.findClassNames(basePackage);
        int controllersCount = 0;

        for (String className : classNames) {
            Class<?> candidate = Class.forName(className);
            if (candidate.isAnnotationPresent(RestController.class)) {
                registerControllerClass(candidate);
                controllersCount++;
            }
        }

        return controllersCount;
    }

    private static void registerControllerClass(Class<?> controllerClass) throws Exception {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("Class is not annotated with @RestController: " + controllerClass.getName());
        }

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                if (!method.getReturnType().equals(String.class)) {
                    throw new IllegalArgumentException("Only String return type is supported for @GetMapping methods: " + method.getName());
                }

                String path = method.getAnnotation(GetMapping.class).value();
                if (routes.containsKey(path)) {
                    throw new IllegalArgumentException("Duplicated @GetMapping path detected: " + path);
                }
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
