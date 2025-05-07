package aibe1.proj2.mentoss.feature.testController.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aiTest")
public class AiTestController {
    @GetMapping("/prompt")
    public String promptTestPage() {
        return "ai-test";
    }
}
