package aibe1.proj2.mentos.feature.initTest.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("msg", "Test message");
        return "index";
    }
}
