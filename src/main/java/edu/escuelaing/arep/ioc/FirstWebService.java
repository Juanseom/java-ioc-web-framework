package edu.escuelaing.arep.ioc;

@RestController
public class FirstWebService {

    @GetMapping("/")
    public String index() {
        return "Greetings from MicroSpringBoot!";
    }
}

