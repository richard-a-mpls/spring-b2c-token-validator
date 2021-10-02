package com.rca.authorization.azureb2ctokenvalidator;

import com.rca.authorization.azureb2ctokenvalidator.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Azureb2ctokenvalidatorApplication {

	private final static Logger logger = LoggerFactory.getLogger("Application");

	public static void main(String[] args) {
		SpringApplication.run(Azureb2ctokenvalidatorApplication.class, args);
		if (System.getenv(Constants.ENV_BASIC_SECURITY) == null) {
			logger.warn("Enhanced security is not enabled.");
		}
	}

}
