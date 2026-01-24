package edu.pjatk.planista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PlanistaApplication {

	public static void main(String[] args) {
		System.out.println("trustStore=" + System.getProperty("javax.net.ssl.trustStore"));
		System.out.println("trustStorePassword set=" + (System.getProperty("javax.net.ssl.trustStorePassword") != null));

		SpringApplication.run(PlanistaApplication.class, args);
	}

}
