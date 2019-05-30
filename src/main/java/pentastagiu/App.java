package pentastagiu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This class is the Main class of the project and the starting point.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@ComponentScan
public class App {

      /**
     * Main method that starts the application.
     * @param args the arguments list for the project
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
