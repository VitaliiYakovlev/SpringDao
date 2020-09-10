package springlecture.part3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringPart3Runner {
    public static void main(String[] args) {
        SpringApplication.run(SpringPart3Runner.class, args);
    }

}
