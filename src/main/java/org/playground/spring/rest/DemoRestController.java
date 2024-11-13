package org.playground.spring.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRestController {
    @GetMapping("/")
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/workout")
    public String workout() {
        return "workout";
    }

    @GetMapping("/fortune")
    public String fortune() {
        return "fortune";
    }
}
