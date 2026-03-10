package edu.escuelaing.arep.ioc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPathScannerTest {

    @Test
    void shouldFindClassesInsideBasePackage() throws Exception {
        List<String> classNames = ClassPathScanner.findClassNames("edu.escuelaing.arep.ioc");

        assertTrue(classNames.contains("edu.escuelaing.arep.ioc.FirstWebService"));
        assertTrue(classNames.contains("edu.escuelaing.arep.ioc.GreetingController"));
    }
}

