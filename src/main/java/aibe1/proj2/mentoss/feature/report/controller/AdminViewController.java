package aibe1.proj2.mentoss.feature.report.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminPage")
public class AdminViewController {
    @GetMapping
    public String adminPage() {
        return "admin";
    }
}
