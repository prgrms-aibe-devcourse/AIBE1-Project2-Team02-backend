package aibe1.proj2.mentoss.feature.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestViewController {

    @GetMapping("/login")
    public String loginTestPage() {
        return "login-test";
    }
}