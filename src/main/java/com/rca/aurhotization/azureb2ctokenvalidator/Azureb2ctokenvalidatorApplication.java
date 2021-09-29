package com.rca.aurhotization.azureb2ctokenvalidator;

import com.rca.aurhotization.azureb2ctokenvalidator.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Azureb2ctokenvalidatorApplication {

	private final static Logger logger = LoggerFactory.getLogger("Application");

	public static void main(String[] args) {
		if (System.getenv(Constants.ENV_KEY) == null && System.getenv(Constants.ENV_SECRET) == null) {
			logger.warn("Enhanced security is not enabled.");
		}
		SpringApplication.run(Azureb2ctokenvalidatorApplication.class, args);
	}

}
