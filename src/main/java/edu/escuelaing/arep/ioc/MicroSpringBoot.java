package edu.escuelaing.arep.ioc;

public class MicroSpringBoot {

    private static final String DEFAULT_BASE_PACKAGE = "edu.escuelaing.arep.ioc";

    public static void main(String[] args) {
        String basePackage = DEFAULT_BASE_PACKAGE;
        if (args.length >= 1) {
            basePackage = args[0];
        }

        try {
            int controllersLoaded = WebComponentRegistry.registerControllersInPackage(basePackage);
            System.out.println("Controllers loaded: " + controllersLoaded);
            HttpServer.start();
        } catch (Exception e) {
            System.out.println("Error starting MicroSpringBoot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
