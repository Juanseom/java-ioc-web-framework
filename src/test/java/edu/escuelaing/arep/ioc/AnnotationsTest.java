package edu.escuelaing.arep.ioc;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationsTest {

    @Test
    void greetingControllerShouldUseGetMappingAndRequestParam() throws Exception {
        Method method = GreetingController.class.getDeclaredMethod("greeting", String.class);
        assertTrue(method.isAnnotationPresent(GetMapping.class));
        assertEquals("/greeting", method.getAnnotation(GetMapping.class).value());

        Parameter parameter = method.getParameters()[0];
        assertTrue(parameter.isAnnotationPresent(RequestParam.class));
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        assertEquals("name", requestParam.value());
        assertEquals("World", requestParam.defaultValue());
    }
}

