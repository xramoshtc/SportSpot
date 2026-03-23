package ioc.dammdev.SportSpotServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal que arrenca el servidor Spring Boot.
 */
@SpringBootApplication
public class SportSpotServerApplication {

    /**
     * Mètode d'entrada de l'aplicació.
     * @param args Arguments de línia de comandes.
     */
	public static void main(String[] args) {
		SpringApplication.run(SportSpotServerApplication.class, args);
	}

}
