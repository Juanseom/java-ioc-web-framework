package edu.escuelaing.arep.ioc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebComponentRegistryTest {

    @BeforeEach
    void clearRoutes() throws Exception {
        Field routesField = WebComponentRegistry.class.getDeclaredField("routes");
        routesField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, WebComponentRegistry.RouteDefinition> routes =
                (Map<String, WebComponentRegistry.RouteDefinition>) routesField.get(null);
        routes.clear();
    }

    @Test
    void shouldRegisterControllersFoundInPackage() throws Exception {
        int controllersLoaded = WebComponentRegistry.registerControllersInPackage("edu.escuelaing.arep.ioc");

        assertTrue(controllersLoaded >= 2);
        assertNotNull(WebComponentRegistry.getRoute("/"));
        assertNotNull(WebComponentRegistry.getRoute("/greeting"));
    }

    @Test
    void shouldRegisterControllerByClassName() throws Exception {
        WebComponentRegistry.registerControllerClass("edu.escuelaing.arep.ioc.FirstWebService");

        assertNotNull(WebComponentRegistry.getRoute("/"));
        assertEquals("index", WebComponentRegistry.getRoute("/").getMethod().getName());
    }
}

