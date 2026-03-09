package edu.escuelaing.arep.ioc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassPathScanner {

    private ClassPathScanner() {
    }

    public static List<String> findClassNames(String basePackage) throws Exception {
        List<String> classNames = new ArrayList<>();
        String packagePath = basePackage.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            if (!"file".equals(resource.getProtocol())) {
                continue;
            }

            File directory = new File(resource.toURI());
            scanDirectory(directory, basePackage, classNames);
        }

        return classNames;
    }

    private static void scanDirectory(File directory, String packageName, List<String> classNames) {
        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), classNames);
                continue;
            }

            if (!file.getName().endsWith(".class")) {
                continue;
            }

            String simpleName = file.getName().substring(0, file.getName().length() - 6);
            if (simpleName.contains("$")) {
                continue;
            }

            classNames.add(packageName + "." + simpleName);
        }
    }
}

