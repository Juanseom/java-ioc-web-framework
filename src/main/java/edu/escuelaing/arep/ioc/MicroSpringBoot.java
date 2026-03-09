package edu.escuelaing.arep.ioc;

public class MicroSpringBoot {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -cp target/classes edu.escuelaing.arep.ioc.MicroSpringBoot <ControllerClassName>");
            return;
        }

        String controllerClassName = args[0];

        try {
            WebComponentRegistry.registerControllerClass(controllerClassName);
            HttpServer.start();
        } catch (Exception e) {
            System.out.println("Error starting MicroSpringBoot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
