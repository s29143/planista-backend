package edu.pjatk.planista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PlanistaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanistaApplication.class, args);
	}

}
