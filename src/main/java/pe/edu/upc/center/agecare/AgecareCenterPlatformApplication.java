package pe.edu.upc.center.agecare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AgecareCenterPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgecareCenterPlatformApplication.class, args);
	}

}
