package aibe1.proj2.mentoss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MentossApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentossApplication.class, args);
    }

}
