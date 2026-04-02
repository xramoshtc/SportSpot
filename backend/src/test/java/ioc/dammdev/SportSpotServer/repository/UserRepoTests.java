package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest // Configura H2 en memòria, Hibernate i Spring Data JPA
@ActiveProfiles("test")
public class UserRepoTests {


    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
    }
    
   
  
    @Test
    public void findByUserNameTest() {
        // GIVEN: Preparem les dades 
        System.out.println("USUARIS ACTUALS A LA BD: " + userRepository.count());
        User admin = new User("admin", "1234", "admin@test.com", "ADMIN");
        userRepository.save(admin);
         System.out.println("USUARIS ACTUALS A LA BD: " + userRepository.count());
          User nou = new User("joanet", "5678", "j@test.com", "USER");
       
        // WHEN
        User guardat = userRepository.save(nou);
         System.out.println("USUARIS ACTUALS A LA BD: " + userRepository.count());

        // WHEN: Executem el mètode del repositori
        Optional<User> trobat = userRepository.findByName("admin");

        // THEN: Verifiquem resultats
        assertThat(trobat).isPresent();
        assertThat(trobat.get().getName()).isEqualTo(admin.getName());
    }

    @Test
    public void saveUsergetIdNotNull() {
        // GIVEN
        User nou = new User("joanet", "5678", "j@test.com", "USER");
        System.out.println("USUARIS ACTUALS A LA BD: " + userRepository.count());
         //WHEN
        User guardat = userRepository.save(nou);
         System.out.println("USUARIS ACTUALS A LA BD: " + userRepository.count());
         //THEN
        assertThat(guardat.getId()).isNotNull();
        assertThat(guardat.getName()).isEqualTo("joanet");
    }
}