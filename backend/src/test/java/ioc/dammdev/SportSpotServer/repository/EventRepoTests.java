package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.Event;
import ioc.dammdev.SportSpotServer.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests d'unitat per al EventRepository.
 * Verifica que les consultes personalitzades i les relacions JPA funcionen segons el disseny.
 * @author Gess Montalbán
 */
@DataJpaTest // Configura H2 en memòria, Hibernate i Spring Data JPA
@ActiveProfiles("test")   
class EventRepoTests {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User organitzador;
    private User participant;
    private Court pista;

    @BeforeEach
    void setUp() {
        // Preparem dades bàsiques: Usuaris i Pista
        organitzador = new User(null, "Boss", "1234", "boss@test.com", "USER", true);
        participant = new User(null, "Player", "1234", "player@test.com", "USER", true);
        pista = new Court(null, "Central", "Pàdel", 20.0,12, "Nord");
        pista.setCapacity(4); // Important per a la lògica de l'entitat

        entityManager.persist(organitzador);
        entityManager.persist(participant);
        entityManager.persist(pista);
        entityManager.flush();
    }

    /**
     * Test que verifica la persistència d'un esdeveniment i la seva recuperació.
     */
    @Test
    void whenSaveEvent_thenCanBeFoundById() {
        Event event = new Event("Torneig Flash", LocalDateTime.now(), organitzador, pista);
        Event saved = eventRepository.save(event);

        Event found = eventRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Torneig Flash");
        assertThat(found.getOrganizer().getName()).isEqualTo("Boss");
    }

    /**
     * Test de la consulta personalitzada per trobar on participa un usuari.
     */
    @Test
    void whenFindByParticipants_Id_thenReturnsCorrectEvents() {
        // GIVEN: Un event on s'apunta el participant
        Event event = new Event("Partit Divendres", LocalDateTime.now(), organitzador, pista);
        event.addParticipant(participant);
        eventRepository.save(event);

        // WHEN: Busquem els events on participa l'usuari 'participant'
        List<List<Event>> results = List.of(eventRepository.findByParticipants_Id(participant.getId()));

        // THEN
        assertThat(results.get(0)).hasSize(1);
        assertThat(results.get(0).get(0).getTitle()).isEqualTo("Partit Divendres");
    }

    /**
     * Test de la consulta per ID d'organitzador.
     */
    @Test
    void whenFindByOrganizer_Id_thenReturnsCorrectEvents() {
        Event event = new Event("Meves Proves", LocalDateTime.now(), organitzador, pista);
        eventRepository.save(event);

        List<Event> organized = eventRepository.findByOrganizer_Id(organitzador.getId());

        assertThat(organized).hasSize(1);
        assertThat(organized.get(0).getOrganizer().getName()).isEqualTo("Boss");
    }

    /**
     * Verifica que si s'esborra un event, no s'esborren els usuaris (Cascading correcte).
     */
    @Test
    void whenDeleteEvent_thenUsersAreNotDeleted() {
        Event event = new Event("Temporal", LocalDateTime.now(), organitzador, pista);
        Event saved = eventRepository.save(event);
        Long eventId = saved.getId();

        eventRepository.deleteById(eventId);
        entityManager.flush();

        assertThat(eventRepository.findById(eventId)).isEmpty();
        assertThat(entityManager.find(User.class, organitzador.getId())).isNotNull();
        assertThat(entityManager.find(Court.class, pista.getId())).isNotNull();
    }
}