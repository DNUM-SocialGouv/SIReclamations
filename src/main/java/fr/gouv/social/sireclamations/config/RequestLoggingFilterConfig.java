package fr.gouv.social.sireclamations.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class RequestLoggingFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilterConfig.class);

    @Bean
    public TomcatContextCustomizer addLogbackAccessValve() {
        return context -> {
            try (InputStream inputStream = getClass().getResourceAsStream("/logback-access.xml")) {
                if (inputStream == null) {
                    throw new NullPointerException("logback-access.xml not found.");
                }
                final Path configFilePath = context.getCatalinaBase().toPath().resolve(LogbackValve.DEFAULT_CONFIG_FILE);
                Files.createDirectories(configFilePath.getParent());
                Files.copy(inputStream, configFilePath);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            final LogbackValve logbackValve = new LogbackValve();
            logbackValve.setQuiet(true);
            context.getPipeline().addValve(logbackValve);
        };
    }
}