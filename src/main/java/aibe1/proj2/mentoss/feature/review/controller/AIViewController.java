package aibe1.proj2.mentoss.feature.review.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aiTest")
public class AIViewController {
    @GetMapping("/prompt")
    public String promptTestPage() {
        return "aiTest";
    }
}
