package pl.akh.domainservicesvc.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * Test controller user to verify connection between services inside cluster
 */
@RestController
@RequestMapping("/public")
public class PublicController {
    @GetMapping("ping")
    public String ping() {
        return "pong";
    }
}
