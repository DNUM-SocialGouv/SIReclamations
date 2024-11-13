package fr.gouv.social.sireclamations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SireclamationsApplications {

	private static final Logger logger = LoggerFactory.getLogger(SireclamationsApplications.class);

	public static void main(String[] args) {
		SpringApplication.run(SireclamationsApplications.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		logger.info("Bienvenue dans la fonction hello!");
		return String.format("Hello %s!", name);
	}
}
