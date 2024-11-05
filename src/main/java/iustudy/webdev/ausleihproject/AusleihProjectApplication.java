package iustudy.webdev.ausleihproject;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableVaadin
public class AusleihProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AusleihProjectApplication.class, args);
    }

}
