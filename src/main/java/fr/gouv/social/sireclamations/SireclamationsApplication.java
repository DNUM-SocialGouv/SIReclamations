package fr.gouv.social.sireclamations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SireclamationsApplication {

	private static final Logger logger = LoggerFactory.getLogger(SireclamationsApplication.class);


	public static void main(String[] args) {

		SpringApplication.run(SireclamationsApplication.class, args);

		logger.info("SIReclamations Application instanciée !");
	}
}