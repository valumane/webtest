package tp.tp1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LDController {

    @GetMapping("/message")
    public String message()
    {
        return "ma chaine de caractere";
    }
}
