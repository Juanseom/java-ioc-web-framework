package edu.escuelaing.arep.ioc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestTest {

    @Test
    void shouldParseQueryParameters() {
        Request request = new Request("GET", "/greeting", "name=Juan&city=Bogota");

        assertEquals("Juan", request.getValue("name"));
        assertEquals("Bogota", request.getValue("city"));
    }

    @Test
    void shouldReturnEmptyStringWhenParameterDoesNotExist() {
        Request request = new Request("GET", "/greeting", "name=Juan");

        assertEquals("", request.getValue("unknown"));
    }
}

